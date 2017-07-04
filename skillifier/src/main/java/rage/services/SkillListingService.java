package rage.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import rage.models.Skill;
import rage.models.User;
import rage.models.UserSkill;
import rage.models.daos.SkillDao;
import rage.models.daos.UserSkillDao;

@Service
@SuppressWarnings("nullness")
public class SkillListingService {
    
    @Autowired SkillDao skillDao;
    @Autowired UserSkillDao userSkillDao;

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
        Map<String, Skill> map = new HashMap<>();
        for (Skill skill : skills) {
            map.put(skill.getName(), skill);
        }
        for (UserSkill userSkill : userSkills) {
            map.get(userSkill.getSkill().getName()).setPercentage(userSkill.getPercentage());
        }
        return skills;
    }

}
