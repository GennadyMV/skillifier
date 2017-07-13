package rage.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rage.exceptions.AuthenticationFailedException;
import rage.models.daos.UserDao;
import rage.services.JsonService;
import rage.services.UserService;

@RestController
public class UserController {
    private final JsonService jsonService;
    private final UserDao userDao;
    private final UserService userService;

    public UserController(JsonService jsonService, UserDao userDao, UserService userService) {
        this.jsonService = jsonService;
        this.userDao = userDao;
        this.userService = userService;
    }

    @RequestMapping("/users")
    public String getUsers() {
        return jsonService.toJson(userDao.findAll());
    }

    @RequestMapping("/debug/oauth")
    public String oauthToUsername(@RequestParam String token) throws AuthenticationFailedException {
        return userService.oauthFromServer(token).getUsername();
    }

}