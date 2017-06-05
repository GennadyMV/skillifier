package rage.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rage.models.Exercise;
import rage.models.ExerciseSkill;
import rage.models.Skill;
import rage.models.User;
import rage.models.UserExercise;
import rage.models.UserSkill;
import rage.models.daos.SkillDao;
import rage.models.daos.UserDao;
import rage.models.daos.UserExerciseDao;
import rage.models.daos.UserSkillDao;

@Service
@Transactional
@SuppressWarnings("nullness")
public class ExerciseAssignmentService {

    @Autowired private SkillDao skillDao;
    @Autowired private UserDao userDao;
    @Autowired private UserExerciseDao userExerciseDao;
    @Autowired private UserSkillDao userSkillDao;
    
    // Configuration
    
    private static final double SKILL_IGNORE_PERCENTAGE = 90;
    
    private void addZeroedSkills(Map<Skill, Double> user, List<Skill> course, List<Skill> ignored) {
        for (Skill skill : course) {
            if (!user.containsKey(skill) && !ignored.contains(skill)) {
                user.put(skill, 0.0);
            }
        }
    }

    private Map<Skill, Double> checkUserSkills(List<Skill> courseSkills, List<UserSkill> userUserSkills) {
        Map<Skill, Double> userKnowledge = new HashMap<>();
        List<Skill> ignoredSkills = new ArrayList<>();
        for (UserSkill userSkill : userUserSkills) {
            if (userSkill.getPercentage() < SKILL_IGNORE_PERCENTAGE) {
                userKnowledge.put(userSkill.getSkill(), userSkill.getPercentage());
            } else {
                ignoredSkills.add(userSkill.getSkill());
            }
        }
        addZeroedSkills(userKnowledge, courseSkills, ignoredSkills);
        return userKnowledge;
    }
    
    private List<Exercise> checkSkillExercises(Skill skill, List<Exercise> userExercises) {
        List<Exercise> exercises = new ArrayList<>();
        for (ExerciseSkill exerciseSkill : skill.getExercises()) {
            exercises.add(exerciseSkill.getExercise());
        }
        for (Exercise exercise : userExercises) {
            exercises.remove(exercise);
        }
        return exercises;
    }
    
    private List<Exercise> findLowest(Map<Double, Skill> sorted, List<Exercise> exercises) {
        List<Exercise> availableExercises = new ArrayList<>();
        Set skillSet = sorted.entrySet();
        Iterator iterator = skillSet.iterator();
        while (iterator.hasNext()) {
            Entry entry = (Entry)iterator.next();
            availableExercises = checkSkillExercises((Skill)entry.getValue(), exercises);
            if (!checkSkillExercises((Skill)entry.getValue(), exercises).isEmpty()) {
                return availableExercises;
            }
        }
        return new ArrayList<>();
    }
    
    private List<Exercise> sortAndFindLowest(Map<Skill, Double> skills, List<Exercise> exercises) {
        Map<Double, Skill> inverse = new HashMap<>();
        for (Entry entry : skills.entrySet()) {
            inverse.put((Double)entry.getValue(), (Skill)entry.getKey());
        }
        Map<Double, Skill> sorted = new TreeMap<>(inverse);
        return findLowest(sorted, exercises);
    }
    
    private List<Exercise> findLowestSkillExercises(Map<Skill, Double> skills, User user) {
        List<UserExercise> userExercises = userExerciseDao.findByUser(user);
        List<Exercise> exercises = new ArrayList<>();
        for (UserExercise userExercise : userExercises) {
            exercises.add(userExercise.getExercise());
        }
        return sortAndFindLowest(skills, exercises);
    }
    
    private List<Exercise> gatherParameters(String courseName, int weekNumber, User user) {
        List<Skill> courseSkills = skillDao.findByCourseAndWeek(courseName, weekNumber);
        List<UserSkill> userSkills = userSkillDao.findByUserAndWeek(weekNumber, user);
        Map<Skill, Double> userKnowledge = checkUserSkills(courseSkills, userSkills);
        return findLowestSkillExercises(userKnowledge, user);
    }
    
    public Exercise getNextExercise(String courseName, int weekNumber, User user) {
        if (user == null) {
            return new UserExercise().createAndReturn().getExercise();
        }
        if (user.getAssignedExercise() == null) {
            List<Exercise> exercises = gatherParameters(courseName, weekNumber, user);
            if (exercises.isEmpty()) {
                return new UserExercise().createAndReturn().getExercise();
            }
            Exercise exercise = exercises.get(new Random().nextInt(exercises.size()));
            user.setAssignedExercise(new UserExercise(user, exercise, false, false));
            userDao.save(user);
        }
        return user.getAssignedExercise().getExercise();
    }
    
}
