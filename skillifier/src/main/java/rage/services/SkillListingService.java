package rage.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.transaction.annotation.Transactional;

import rage.models.Skill;
import rage.models.User;
import rage.models.UserSkill;
import rage.models.daos.SkillDao;
import rage.models.daos.UserSkillDao;



@Service
@Transactional
public class SkillListingService {
    
    final SkillDao skillDao;
    final UserSkillDao userSkillDao;

    @Autowired
    public SkillListingService(SkillDao skillDao, UserSkillDao userSkillDao) {
        this.skillDao = skillDao;
        this.userSkillDao = userSkillDao;
    }

    /**
     * Find the Skills of the specified Course (per User) to display in the Netbeans plugin Timeline.
     * This includes zero-rows, so if the User hasn't started doing anything, all the Skills shall
     * still be displayed, though only at level 0.00% sureness.
     * @param courseName The specified Course name that is the parent of the Skills
     * @param user The User whose Skills you want to fetch
     * @return (List of type Skill) The list of Skills
     */
    public List<Skill> getSkillsWithUserSpecificInformation(@PathVariable String courseName, User user) {
        List<Skill> skills = skillDao.findByCourse(courseName);
        List<UserSkill> userSkills = userSkillDao.findByUser(user);
        userSkills.forEach(userSkill -> {
            if (skills.contains(userSkill)) {
                skills.get(skills.indexOf(userSkill)).setPercentage(userSkill.getPercentage());
            }
        });

        return skills;
    }

}
