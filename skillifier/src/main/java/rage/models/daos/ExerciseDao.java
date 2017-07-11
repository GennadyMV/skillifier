package rage.models.daos;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import rage.models.Exercise;

@Transactional
public interface ExerciseDao extends CrudRepository<Exercise, Long> {

    /**
     * Find and return all Exercises related to a certain Course.
     * @param courseName The name of the Course as a String.
     * @return (List of type Exercise) A list of Exercises related to the Course specified.
     */
    @Query("SELECT DISTINCT e FROM Exercise e, ExerciseSkill es, Skill s, Theme t, Week w, Course c"
            + " WHERE c.id = w.course AND w.id = t.week"
            + " AND t.id = s.theme AND s.id = es.exerciseSkill AND c.name = :courseName")
    List<Exercise> findExercisesByCourse(@Param("courseName") String courseName);
    
}
