package rage.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rage.exceptions.AuthenticationFailedException;
import rage.models.Skill;
import rage.models.daos.UserDao;
import rage.services.JsonService;
import rage.services.SkillListingService;
import rage.services.UserService;

import java.io.Serializable;
import java.util.List;

@RestController
public class SkillController {
    private final JsonService jsonService;
    private final SkillListingService skillListingService;
    private final UserService userService;
    private final UserDao userDao;

    public SkillController(JsonService jsonService, SkillListingService skillListingService,
                           UserService userService, UserDao userDao) {
        this.jsonService = jsonService;
        this.skillListingService = skillListingService;
        this.userService = userService;
        this.userDao = userDao;
    }

    @RequestMapping("courses/{courseName}/skills")
    public List<Skill> getCourseSkills(@PathVariable String courseName, @RequestParam String token) throws AuthenticationFailedException {
        return skillListingService
                .getSkillsWithUserSpecificInformation(courseName, userService.oauthFromServer(token));
    }

    @RequestMapping("/users/{username}/skills")
    public String getUserSkills(@PathVariable String username) {
        return jsonService.toJson(userDao.findUserSkills(username));
    }
}