package rage.controllers;

import org.apache.http.auth.AUTH;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rage.exceptions.AuthenticationFailedException;
import rage.models.User;
import rage.models.UserExercise;
import rage.models.daos.ExerciseDao;
import rage.models.daos.UserExerciseDao;
import rage.services.ExerciseAssignmentService;
import rage.services.ExerciseListingService;
import rage.services.ExerciseSubmissionService;
import rage.services.JsonService;
import rage.services.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

@RestController
public class ExerciseController {
    private final JsonService jsonService;
    private final ExerciseAssignmentService exerciseAssignmentService;
    private final UserService userService;
    private final ExerciseListingService exerciseListingService;
    private final ExerciseDao exerciseDao;
    private final UserExerciseDao userExerciseDao;
    private final ExerciseSubmissionService exerciseSubmissionService;

    public ExerciseController(JsonService jsonService, ExerciseAssignmentService exerciseAssignmentService,
                              UserService userService, ExerciseListingService exerciseListingService,
                              ExerciseDao exerciseDao, UserExerciseDao userExerciseDao,
                              ExerciseSubmissionService exerciseSubmissionService) {
        this.jsonService = jsonService;
        this.exerciseAssignmentService = exerciseAssignmentService;
        this.userService = userService;
        this.exerciseListingService = exerciseListingService;
        this.exerciseDao = exerciseDao;
        this.userExerciseDao = userExerciseDao;
        this.exerciseSubmissionService = exerciseSubmissionService;
    }


    @RequestMapping(value = "/courses/{courseName}/{partNumber}/next.json", params = {"token"})
    public String getNextExercise(
            @PathVariable String courseName, @PathVariable int partNumber, @RequestParam String token) throws AuthenticationFailedException {
        return jsonService.toJson(exerciseAssignmentService.getNextExercise(courseName, partNumber,
                userService.oauthFromServer(token)));
    }

    @RequestMapping("/courses/{courseName}/users/{userId}/exercises")
    public String getUsersCourseExercises(@PathVariable String courseName, @RequestParam String token) throws AuthenticationFailedException {
        return jsonService.toJson(exerciseListingService
                .addUserSpecificInfoToExercises(userService.oauthFromServer(token), courseName));
    }

    @RequestMapping(value = "/download", params = {"token"}, produces = "application/zip")
    public void downloadExercise(HttpServletResponse response,
                                        @RequestParam String token) throws Throwable {
        User user = userService.oauthFromServer(token);
        Optional<UserExercise> exerciseAssignment = user.getAssignedExercise();
        if (!exerciseAssignment.isPresent()) {
            // Handle exception

            return;
        }
        UserExercise assignedExercise = exerciseAssignment.get();
        response.setHeader("Content-Disposition", "attachment; filename="
                + assignedExercise.getExercise().getName());
        Files.copy(Paths.get(assignedExercise.getExercise().getDownloadUrl()), response.getOutputStream());
    }

    @RequestMapping("/courses/{courseName}/exercises")
    public String getCourseExercises(@PathVariable String courseName) {
        return jsonService.toJson(exerciseDao.findExercisesByCourse(courseName));
    }

    @RequestMapping("/users/{username}/exercises")
    public String getUserExercises(@PathVariable String username) {
        return jsonService.toJson(userExerciseDao.findByUser(userService.setupUser(username)));
    }

    @RequestMapping(value = "submitZip", params = {"token"}, method = RequestMethod.POST)
    public void submitExerciseZipByOAuth(@RequestParam String token,
                                                              @RequestBody byte[] zip) throws IOException, AuthenticationFailedException {
        exerciseSubmissionService.submitExerciseZip(token, zip);
    }
}
