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

@SpringBootTest
@SuppressWarnings("nullness")
@RunWith(SpringJUnit4ClassRunner.class)
public class ExerciseAssignmentServiceTest {

    static {
        Skillifier.setLocalTestProperties();
    }

    private Course course;
    private int weekNumber;
    private User user;
    
    @Autowired private CourseDao courseDao;
    @Autowired private UserDao userDao;
    @Autowired private ExerciseAssignmentService assignmentService;
    
    @Before
    public void initialize() {
        course = courseDao.findByName("ohtu-ohpe");
        weekNumber = 1;
        user = userDao.findByUsername("Saku");
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
        user.setAssignedExercise(null);
        assertNull(user.getAssignedExercise());
    }
    
}
