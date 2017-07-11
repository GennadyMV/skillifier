package rage.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rage.models.daos.CourseDao;
import rage.services.JsonService;

@RestController
public class CourseController {
    private final JsonService jsonService;
    private final CourseDao courseDao;

    public CourseController(JsonService jsonService, CourseDao courseDao) {
        this.jsonService = jsonService;
        this.courseDao = courseDao;
    }

    @RequestMapping("/courses")
    public String getCourses() {
        return jsonService.toJson(courseDao.findAll());
    }
}
