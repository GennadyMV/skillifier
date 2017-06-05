package rage.models.daos;

import org.springframework.data.repository.CrudRepository;
import rage.models.ExerciseSkill;

import javax.transaction.Transactional;


@Transactional
public interface ExerciseSkillDao extends CrudRepository<ExerciseSkill, Long> {
    
    // Only exists to Save stuff currently.
    
}
