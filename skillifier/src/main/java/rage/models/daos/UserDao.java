package rage.models.daos;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import rage.models.User;
import rage.models.UserSkill;

@Transactional
public interface UserDao extends CrudRepository<User, Long> {
    
    /**
     * Find and return User by username.
     * @param username Username as a String
     * @return (User) found User, or null if one doesn't exist
     */
    public User findByUsername(String username);
    
    /**
     * Find the Skills that the User currently has/is tracking.
     * "Is tracking" means there is some data inserted into the database
     * regarding User and their skills. The database currently does not
     * support zero-rows, so if the User hasn't been assigned an Exercise
     * to complete, this method will return an empty list.
     * @param username Username as a String
     * @return (List of type UserSkill) List of Skills in a UserSkill @ManyToMany wrapper Class
     */
    @Query("SELECT DISTINCT us FROM Skill s, UserSkill us, User u"
            + " WHERE s.id = us.skill AND us.user = u AND u.username = :username")
    public List<UserSkill> findUserSkills(@Param("username") String username);
    
}
