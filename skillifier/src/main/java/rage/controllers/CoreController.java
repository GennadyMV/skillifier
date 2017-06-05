package rage.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rage.models.User;
import rage.models.UserExercise;
import rage.models.http.SandboxResult;
import rage.models.http.SubmissionResult;

@RestController
@SuppressWarnings("nullness")
public class CoreController extends BaseController {

    @RequestMapping(value = "submitZip", params = {"token"}, method = RequestMethod.POST)
    public Map<String, Serializable> submitExerciseZipByOAuth(@RequestParam String token, @RequestBody byte[] zip) throws IOException {
        return submissionService.submitExerciseZip(token, zip);
    }
    
    @RequestMapping("/courses/{courseName}/uexercises")
    public String getCourseExercisesByOAuth(@PathVariable String courseName, @RequestParam String token) {
        return jsonService.toJson(listingService.addUserSpecificInfoToExercises(userService.oauthFromServer(token), courseName));
    }
    
    @RequestMapping(value = "{courseName}/{weekNumber}/next.json", params = {"username"})
    public String getNextExercise(
            @PathVariable String courseName, @PathVariable int weekNumber, @RequestParam String username) {
        return jsonService.toJson(assignmentService.getNextExercise(courseName, weekNumber, userService.setupUser(username)));
    }
    
    @RequestMapping(value = "{courseName}/{weekNumber}/next.json", params = {"token"})
    public String getNextExerciseByOAuth(
            @PathVariable String courseName, @PathVariable int weekNumber, @RequestParam String token) {
        return jsonService.toJson(assignmentService.getNextExercise(courseName, weekNumber, userService.oauthFromServer(token)));
    }

    @RequestMapping(value = "/download", params = {"username"}, produces = "application/zip")
    public void downloadExercise(HttpServletResponse response,
                                 @RequestParam String username) throws Throwable {
        User user = userService.setupUser(username);
        UserExercise userExercise = user.getAssignedExercise();
        if (userExercise == null) {
            // Handle exception
            return;
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + userExercise.getExercise().getName());
        Files.copy(Paths.get(userExercise.getExercise().getDownloadUrl()), response.getOutputStream());
    }

    @RequestMapping(value = "/download", params = {"token"}, produces = "application/zip")
    public void downloadExerciseByOauth(HttpServletResponse response,
                                 @RequestParam String token) throws Throwable {
        User user = userService.oauthFromServer(token);
        UserExercise userExercise = user.getAssignedExercise();
        if (userExercise == null) {
            // Handle exception
            return;
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + userExercise.getExercise().getName());
        Files.copy(Paths.get(userExercise.getExercise().getDownloadUrl()), response.getOutputStream());
    }

    @RequestMapping(value = "/result", method = RequestMethod.POST)
    public void receiveResultFromSandbox(
            @RequestParam(name = "exit_code", required = false) String exitCode,
            @RequestParam(name = "status") String status,
            @RequestParam(name = "stderr", required = false) String stderr,
            @RequestParam(name = "stdout", required = false) String stdout,
            @RequestParam(name = "test_output", required = false) String testOutput,
            @RequestParam(name = "token", required = false) String token) throws IOException {
        SandboxResult sandboxResult = new SandboxResult(status, exitCode, token, testOutput, stdout, stderr);
        submissionService.reactToResult(sandboxResult.getToken(), sandboxResult);
        System.out.println(exitCode);
    }
    
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public Map<String, Serializable> resultsToCore(@RequestParam String token) throws IOException {
        SandboxResult sandboxResult = submissionService.getResult(token);
        if (sandboxResult == null) {
            Map<String, Serializable> map = new TreeMap<>();
            map.put("status", "PROCESSING");
            return map;
        }
        SubmissionResult submissionResult = (SubmissionResult) jsonService.fromJson(sandboxResult.getTestOutput(), SubmissionResult.class);
        return submissionResult.toCoreJson();
    }   

}
