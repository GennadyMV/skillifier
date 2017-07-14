package rage.services;

import com.sun.tools.javac.util.Pair;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import rage.exceptions.AuthenticationFailedException;
import rage.models.Exercise;
import rage.models.ExerciseSkill;
import rage.models.User;
import rage.models.UserExercise;
import rage.models.UserSkill;
import rage.models.daos.UserDao;
import rage.models.daos.UserExerciseDao;
import rage.models.daos.UserSkillDao;
import rage.models.http.SandboxResult;
import rage.models.http.SubmissionResult;
import rage.services.files.FileUtils;

@Service
public class ExerciseSubmissionService {
    
    private final Map<String, Optional<SandboxResult>> submissions;
    private final List<String> inSubmission;
    private final ArrayDeque<Pair<String, Path>> waitingSubmission;
    private final UserExerciseDao userExerciseDao;
    private final UserSkillDao userSkillDao;
    private final UserService userService;
    private final FileUtils fileUtils;
    private final UserDao userDao;
    private final JsonService jsonService;

    @Autowired
    public ExerciseSubmissionService(UserExerciseDao userExerciseDao, UserSkillDao userSkillDao, UserService userService,
                                     FileUtils fileUtils, UserDao userDao, JsonService jsonService) {
        this.inSubmission = new ArrayList<>();
        this.submissions = new HashMap();
        this.waitingSubmission = new ArrayDeque<>();
        this.userExerciseDao = userExerciseDao;
        this.userSkillDao = userSkillDao;
        this.userService = userService;
        this.fileUtils = fileUtils;
        this.userDao = userDao;
        this.jsonService = jsonService;
    }


    private void submitExerciseFile(String token, Path tarPath) {
        try {
            if (inSubmission.size() >= 4) {
                waitingSubmission.add(new Pair<>(token, tarPath));
            }
            inSubmission.add(token);
            HttpResponse response = sendTarToSandbox(tarPath.toFile(), token);
            submissions.put(token, Optional.empty());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void submitExerciseZip(String token, byte[] zip) throws AuthenticationFailedException {
        User user = userService.oauthFromServer(token);
        Path temp = null;
        Path tarPath = null;
        try {
            String localSubmissions = System.getProperty("server.local.submissions");
            if (localSubmissions == null) {
                throw new RuntimeException("server.local.submissions is not defined!");
            }
            Path submissionFolder = Paths.get(localSubmissions);
            temp = Files.createTempDirectory(Paths.get(submissionFolder.toString()), "");
            Path target = Files.createTempFile(temp, "project", ".zip");
            Files.write(target, zip);
            Exercise excercise = user.getAssignedExercise().get().getExercise();
            tarPath = fileUtils.decompressProjectAndCreateTar(target,
                    Paths.get(excercise.getDownloadUrl()));
            submitExerciseFile(token, tarPath);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (temp != null) {
                fileUtils.recursiveDelete(temp);
            }
        }
    }
    
    public void reactToResult(String token, SandboxResult result) throws AuthenticationFailedException {
        inSubmission.remove(token);
        submissions.put(token, Optional.of(result));

        try {
            SubmissionResult submissionResult = (SubmissionResult)jsonService.fromJson(result.getTestOutput(), SubmissionResult.class);
            User user = userService.oauthFromServer(token);
            UserExercise userExercise = user.getAssignedExercise().get();
            userExercise.setAttempted(true);
            if (submissionResult.getStatus().toLowerCase().equals("passed")) {
                Exercise exercise = userExercise.getExercise();

                for (ExerciseSkill skill : exercise.getSkills()) {
                    UserSkill userSkill = userSkillDao.findByUserAndSkill(user, skill.getSkill());
                    if (userSkill == null) {
                        userSkill = new UserSkill(user, skill.getSkill(), 0);
                    }
                    userSkill.addSureness(30);
                    userSkillDao.save(userSkill);
                }

                userExercise.setCompleted(true);
                userExercise.setReturnable(false);

                user.setAssignedExercise(Optional.empty());

                userDao.save(user);
            }
            userExerciseDao.save(userExercise);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (inSubmission.size() < 4 && !waitingSubmission.isEmpty()) {
            Pair<String, Path> toSubmit = waitingSubmission.pop();
            submitExerciseFile(toSubmit.fst, toSubmit.snd);
        }
    }
    
    public Optional<SandboxResult> getResult(String token) {
        if (submissions.containsKey(token)) {
            Optional<SandboxResult> sandboxResult = submissions.get(token);
            submissions.remove(token);
            return sandboxResult;
        }
        return Optional.empty();
    }

    public HttpResponse sendTarToSandbox(File tar, String token) throws IOException {
        byte[] data = Files.readAllBytes(tar.toPath());
        System.out.println("kikuli");
        final MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        entityBuilder.addPart("file", new ByteArrayBody(data, ContentType.APPLICATION_OCTET_STREAM, tar.getName()));
        // TODO: To not use Localhost
        entityBuilder.addTextBody("notify", "http://localhost:3000/result", ContentType.create("text/plain", "utf-8"));
        entityBuilder.addTextBody("token", token, ContentType.create("text/plain", "utf-8"));

        String externalSandbox = System.getProperty("server.external.sandbox");
        if (externalSandbox == null) {
            throw new RuntimeException("server.external.sandbox is not defined!");
        }
        HttpPost request = new HttpPost(externalSandbox);
        request.setEntity(entityBuilder.build());
        HttpResponse response = HttpClients.createDefault().execute(request);
        return response;
    }

}
