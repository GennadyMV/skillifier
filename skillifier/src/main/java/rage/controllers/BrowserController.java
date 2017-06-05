package rage.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SuppressWarnings("nullness")
public class BrowserController extends BaseController {

    @RequestMapping("/users")
    public String getUsers() {
        return jsonService.toJson(userDao.findAll());
    }

    @RequestMapping("/courses")
    public String getCourses() {
        return jsonService.toJson(courseDao.findAll());
    }

    @RequestMapping("courses/{courseName}/skills")
    public String getCourseSkills(@PathVariable String courseName, @RequestParam String token) {
        return jsonService.toJson(skillListingService.getSkillsWithUserSpecificInformation(courseName, userService.oauthFromServer(token)));
    }

    @RequestMapping("/courses/{courseName}/exercises")
    public String getCourseExercises(@PathVariable String courseName) {
        return jsonService.toJson(exerciseDao.findExercises(courseName));
    }

    @RequestMapping("/users/{username}/skills")
    public String getUserSkills(@PathVariable String username) {
        return jsonService.toJson(userDao.findUserSkills(username));
    }

    @RequestMapping("/users/{username}/exercises")
    public String getUserExercises(@PathVariable String username) {
        return jsonService.toJson(userExerciseDao.findByUser(userService.setupUser(username)));
    }

    @RequestMapping("/debug/oauth")
    public String oauthToUsername(@RequestParam String token) {
        return userService.oauthFromServer(token).getUsername();
    }

}
