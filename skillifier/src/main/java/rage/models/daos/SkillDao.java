package rage.models.daos;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rage.models.Skill;

import javax.transaction.Transactional;

@Transactional
public interface SkillDao extends CrudRepository<Skill, Long> {
    
    /**
     * Find and return a list of Skills relating to a Week of a specified Course.
     * @param courseName The Course's mame that includes the Week.
     * @param weekNumber The specified Week of the Course.
     * @return (List of type Skill) A list of Skills related to the specified Course and Week.
     */
    @Query("SELECT s FROM Course c, Week w, Theme t, Skill s"
            + " WHERE s.theme = t.id AND t.week = w.id AND w.course = c.id"
            + " AND w.weekNumber = :weekNumber AND c.name = :courseName")
    List<Skill> findByCourseAndWeek(
            @Param("courseName") String courseName, @Param("weekNumber") int weekNumber);

    @Query("SELECT s FROM Theme t, Skill s WHERE s.theme = t.id AND t.name = :themeName")
    List<Skill> findByTheme(@Param("themeName") String themeName);

    @Query("SELECT s FROM Course c, Week w, Theme t, Skill s"
            + " WHERE s.theme = t.id AND t.week = w.id AND w.course = c.id"
            + " AND c.name = :courseName")
    List<Skill> findByCourse(@Param("courseName") String courseName);

}
