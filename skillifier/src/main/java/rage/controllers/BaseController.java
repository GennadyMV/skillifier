package rage.controllers;

import org.springframework.stereotype.Component;
import rage.models.daos.CourseDao;
import rage.models.daos.ExerciseDao;
import rage.models.daos.UserExerciseDao;

import org.springframework.beans.factory.annotation.Autowired;
import rage.models.daos.UserDao;
import rage.services.ExerciseAssignmentService;
import rage.services.ExerciseListingService;
import rage.services.ExerciseSubmissionService;
import rage.services.JsonService;
import rage.services.SkillListingService;
import rage.services.UserService;

@Component
@SuppressWarnings("nullness")
public class BaseController {

    @Autowired protected UserService userService;
    @Autowired protected JsonService jsonService;
    @Autowired protected CourseDao courseDao;
    @Autowired protected ExerciseDao exerciseDao;
    @Autowired protected UserDao userDao;
    @Autowired protected UserExerciseDao userExerciseDao;
    @Autowired protected SkillListingService skillListingService;
    @Autowired protected ExerciseAssignmentService assignmentService;
    @Autowired protected ExerciseListingService listingService;
    @Autowired protected ExerciseSubmissionService submissionService;
    
}
