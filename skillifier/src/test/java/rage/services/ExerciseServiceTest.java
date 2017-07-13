package rage.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rage.Skillifier;
import rage.models.Course;
import rage.models.User;
import rage.models.UserExercise;
import rage.models.daos.CourseDao;
import rage.models.daos.UserDao;

import java.util.Optional;

@SpringBootTest

@RunWith(SpringJUnit4ClassRunner.class)
public class ExerciseServiceTest {
    
    static {
        Skillifier.setLocalTestProperties();
    }

    private Course course;
    private int weekNumber;
    private User user;
    
    private final CourseDao courseDao;
    private final UserDao userDao;
    private final ExerciseAssignmentService assignmentService;

    @Autowired
    public ExerciseServiceTest(CourseDao courseDao, UserDao userDao, ExerciseAssignmentService assignmentService) {
        this.courseDao = courseDao;
        this.userDao = userDao;
        this.assignmentService = assignmentService;
        this.course = courseDao.findByName("ohtu-ohpe");
        this.weekNumber = 1;
        this.user = userDao.findByUsername("Saku");
    }
    
    @Test
    public void userTrackingExercise() {
        // Find Exercise for User
        assignmentService.getNextExercise(course.getName(), weekNumber, user);
        assertNotNull(user.getAssignedExercise());
        // Make sure Exercise has been assigned
        UserExercise exercise = user.getAssignedExercise().get();
        assignmentService.getNextExercise(course.getName(), weekNumber, user);
        assertEquals(exercise, user.getAssignedExercise());
        // Simulate User submitting the Exercise
        user.setAssignedExercise(Optional.empty());
        assertNull(user.getAssignedExercise());
    }
    
}
