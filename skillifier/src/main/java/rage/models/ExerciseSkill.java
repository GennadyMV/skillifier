package rage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Exercise_Skill")
public class ExerciseSkill implements Serializable {
    
    public ExerciseSkill(Exercise exercise, Skill skill) {
        this.skillExercise = exercise;
        this.exerciseSkill = skill;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @ManyToOne
    @JoinColumn(name = "exercise")
    private Exercise skillExercise;
    
    @ManyToOne
    @JoinColumn(name = "skill")
    private Skill exerciseSkill;
    
    // Getters and Setters
    
    @JsonIgnore
    public Exercise getExercise() {
        return skillExercise;
    }
    
    public Skill getSkill() {
        return exerciseSkill;
    }
    
}
