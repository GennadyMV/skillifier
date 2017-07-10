package rage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rage.models.Exercise;
import rage.models.User;
import rage.models.UserExercise;
import rage.models.daos.ExerciseDao;
import rage.models.daos.UserExerciseDao;

@Service
@Transactional
@SuppressWarnings("nullness")
public class ExerciseListingService {
    
    @Autowired ExerciseDao exerciseDao;
    @Autowired UserExerciseDao userExerciseDao;

    /**
     * Find the Exercises of the specified Course (per User) to inform TMC-Core about
     * the state of the Exercises, such as (attempted, completed).
     * @param user The User whose Skills you want to fetch
     * @param courseName The specified Course name that is the parent of the Exercises
     * @return (List of type Exercise) The list of Exercises
     */
    public List<Exercise> addUserSpecificInfoToExercises(User user, String courseName) {
        List<Exercise> exercises = exerciseDao.findExercisesByCourse(courseName);
        List<UserExercise> userExercises = userExerciseDao.findByUserAndCourseName(user, courseName);
        Map<String, Exercise> map = new HashMap<>();
        exercises.forEach(exercise -> map.put(exercise.getName(), exercise));
        setExerciseFields(userExercises, map);
        return exercises;
    }

    private void setExerciseFields(List<UserExercise> userExercises, Map<String, Exercise> map) {
        for (UserExercise userExercise : userExercises) {
            map.get(userExercise.getExercise().getName()).setAttempted(userExercise.isAttempted());
            map.get(userExercise.getExercise().getName()).setCompleted(userExercise.isCompleted());
            map.get(userExercise.getExercise().getName()).setAllReviewPointsGiven(userExercise.isAllReviewPointsGiven());
            map.get(userExercise.getExercise().getName()).setReturnable(userExercise.isReturnable());
        }
    }
    
}
