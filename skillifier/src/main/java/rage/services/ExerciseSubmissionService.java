package rage.services;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@SuppressWarnings("nullness")
public class ExerciseSubmissionService {
    
    public final Map<String, SandboxResult> submissions = new HashMap();
    public List<String> inSubmission;
    public ArrayDeque<Pair<String, Path>> waitingSubmission;

    @Autowired private UserExerciseDao userExerciseDao;
    @Autowired private UserSkillDao userSkillDao;
    @Autowired private UserService userService;
    @Autowired private final FileUtils fileUtils;
    @Autowired private UserDao userDao;
    @Autowired private JsonService jsonService;

    public ExerciseSubmissionService() {
        fileUtils = new FileUtils();
        inSubmission = new ArrayList<>();
        waitingSubmission = new ArrayDeque<>();
    }

    private Map<String, Serializable> submitExerciseFile(String token, Path tarPath) {
        try {
            if (inSubmission.size() >= 4) {
                waitingSubmission.add(new Pair<>(token, tarPath));
                return new HashMap<>();
            }
            inSubmission.add(token);
            HttpResponse response = sendTarToSandbox(tarPath.toFile(), token);
            submissions.put(token, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new HashMap<>();
    }

    public Map<String, Serializable> submitExerciseZip(String token, byte[] zip) {
        User user = userService.oauthFromServer(token);
        Path temp = null;
        Path tarPath = null;
        try {
            Path submissionFolder = Paths.get(System.getProperty("server.local.submissions"));
            temp = Files.createTempDirectory(Paths.get(submissionFolder.toString()), "");
            Path target = Files.createTempFile(temp, "project", ".zip");
            Files.write(target, zip);
            Exercise excercise = user.getAssignedExercise().getExercise();
            tarPath = fileUtils.decompressProjectAndCreateTar(target.toFile(),
                    Paths.get(excercise.getDownloadUrl()));
            return submitExerciseFile(token, tarPath);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            fileUtils.deleteFolderAndItsContent(temp.toFile());
        }
        return new HashMap<>();
    }
    
    public void reactToResult(String token, SandboxResult result) {
        inSubmission.remove(token);
        submissions.put(token, result);

        try {
            SubmissionResult submissionResult = (SubmissionResult)jsonService.fromJson(result.getTestOutput(), SubmissionResult.class);
            User user = userService.oauthFromServer(token);
            UserExercise userExercise = user.getAssignedExercise();
            userExercise.setAttempted(true);
            if (submissionResult.status.toLowerCase().equals("passed")) {
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
                userExercise.setAllReviewPointsGiven(true);
                userExercise.setReturnable(false);

                user.setAssignedExercise(null);

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
    
    public SandboxResult getResult(String token) {
        if (submissions.containsKey(token)) {
            SandboxResult sandboxResult = submissions.get(token);
            submissions.remove(token);
            return sandboxResult;
        }
        return null;
    }

    public HttpResponse sendTarToSandbox(File tar, String token) throws IOException {
        byte[] data = Files.readAllBytes(tar.toPath());

        final MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        entityBuilder.addPart("file", new ByteArrayBody(data, ContentType.APPLICATION_OCTET_STREAM, tar.getName()));
        // TODO: To not use Localhost
        entityBuilder.addTextBody("notify", "http://localhost:3000/result", ContentType.create("text/plain", "utf-8"));
        entityBuilder.addTextBody("token", token, ContentType.create("text/plain", "utf-8"));

        HttpPost request = new HttpPost(System.getProperty("server.external.sandbox"));
        request.setEntity(entityBuilder.build());
        HttpResponse response = HttpClients.createDefault().execute(request);
        return response;
    }

}
