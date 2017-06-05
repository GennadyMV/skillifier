package rage.models.daos;

import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rage.models.Course;

@Transactional
public interface CourseDao extends CrudRepository<Course, Long> {
    
    /**
     * Return a Course with a name specified, or null if one doesn't exist.
     * @param courseName The name of the Course as a String
     * @return (Course) Course with the corresponding name
     */
    public Course findByName(@Param("courseName") String courseName);
    
}
