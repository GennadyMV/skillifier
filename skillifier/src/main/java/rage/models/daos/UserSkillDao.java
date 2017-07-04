package rage.models.daos;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import javax.transaction.Transactional;
import org.springframework.data.repository.query.Param;

import rage.models.Skill;
import rage.models.User;
import rage.models.UserSkill;

@Transactional
public interface UserSkillDao extends CrudRepository<UserSkill, Long> {

    /**
     * Find and return a list of Skills that the user has*.
     * (* This means get all data (Skills) there exists for this User.)
     * @param weekNumber The specified Week parent for the Skills
     * @param user The User whose Skills you want to fetch
     * @return (List of type UserSkill) The list of Skills in UserSkill @ManyToMany wrapper Class
     */
    @Query("SELECT us FROM Week w, Theme t, Skill s, UserSkill us"
            + " WHERE s.id = us.skill AND us.user = :user AND t.week = w.id"
            + " AND s.theme = t.id AND w.weekNumber = :weekNumber")
    public List<UserSkill> findByUserAndWeek(
            @Param("weekNumber") int weekNumber, @Param("user") User user);
    
    /**
     * Find a specified Skill from the User.
     * If the User doesn't have the Skill marked, return null.
     * Otherwise, fetch the Skill.
     * @param user The User whose Skills you want to fetch
     * @param skill The Skill you're looking for
     * @return The Skill in UserSkill @ManyToMany wrapper Class
     */
    public UserSkill findByUserAndSkill(User user, Skill skill);

    /**
     * Find all the Skills the User has marked as started/done.
     * @param user The User whose Skills you want to fetch
     * @return (List of type UserSkill) The list of Skills in UserSkill @ManyToMany wrapper Class
     */
    public List<UserSkill> findByUser(User user);

}
