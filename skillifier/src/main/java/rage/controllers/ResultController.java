package rage.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rage.models.http.SandboxResult;
import rage.models.http.SubmissionResult;
import rage.services.ExerciseSubmissionService;
import rage.services.JsonService;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

@RestController
public class ResultController {
    private final ExerciseSubmissionService exerciseSubmissionService;
    private final JsonService jsonService;

    public ResultController(ExerciseSubmissionService exerciseSubmissionService, JsonService jsonService) {
        this.exerciseSubmissionService = exerciseSubmissionService;
        this.jsonService = jsonService;
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
        exerciseSubmissionService.reactToResult(sandboxResult.getToken(), sandboxResult);
        System.out.println(exitCode);
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public Map<String, Serializable> resultsToCore(@RequestParam String token) throws IOException {
        SandboxResult sandboxResult = exerciseSubmissionService.getResult(token);
        if (sandboxResult == null) {
            Map<String, Serializable> map = new TreeMap<>();
            map.put("status", "PROCESSING");
            return map;
        }
        SubmissionResult submissionResult = (SubmissionResult) jsonService
                .fromJson(sandboxResult.getTestOutput(), SubmissionResult.class);
        return submissionResult.toCoreJson();
    }
}
