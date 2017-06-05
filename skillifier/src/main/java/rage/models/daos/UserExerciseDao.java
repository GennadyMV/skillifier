package rage.models.daos;

import java.util.List;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import rage.models.User;
import rage.models.UserExercise;

@Transactional
public interface UserExerciseDao extends CrudRepository<UserExercise, Long> {
        
    /**
     * Find and return a list of already completed (or attempted*)
     * Exercises the User has marked in the database.
     * (* Since the database doesn't include zero-rows, any data -
     * such as (attempted, assigned) - will be included in this list.)
     * @param user The User whose Exercises you want to fetch
     * @return (List of type UserExercise) The list of Exercises in UserExercise @ManyToMany wrapper Class
     */
    public List<UserExercise> findByUser(@Param("user") User user);

    /**
     * Find and return a list of Exercises the User has tracked/is currently tracking
     * (See: JavaDoc for .findByUser()) from a specified Course for User specific information.
     * @param user The User whose Exercises you want to fetch
     * @param courseName The specified Course's name from where to get the Exercises
     * @return (List of type UserExercise) The list of Exercises in UserExercise @ManyToMany wrapper Class
     */
    @Query("SELECT DISTINCT ue from UserExercise ue, Exercise e, Skill s, Theme t, Week w, Course c, ExerciseSkill es"
            + " WHERE ue.exercise = e.id AND s.theme = t.id AND t.week = w.id AND w.course = c.id"
            + " AND es.exerciseSkill = s.id AND es.skillExercise = e.id"
            + " AND c.name = :courseName AND ue.user = :user")
    public List<UserExercise> findByUserAndCourseName(@Param("user") User user, @Param("courseName") String courseName);

    
}
