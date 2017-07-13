package rage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Skill")
public class Skill implements Serializable {
    
    public Skill(String name, Theme theme) {
        this.name = name;
        this.theme = theme;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "theme")
    private Theme theme;

    @OneToMany(mappedBy = "exerciseSkill")
    private Set<ExerciseSkill> exercises = new HashSet<>();
    
    @OneToMany(mappedBy = "user")
    private Set<UserSkill> users = new HashSet<>();

    @Transient
    private double percentage;
    
    // Getters and Setters
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public Theme getTheme() {
        return theme;
    }

    public String getThemeName() {
        return theme.getName();
    }

    public int getWeekNumber() {
        return theme.getWeek().getWeekNumber();
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    @JsonIgnore
    public Set<ExerciseSkill> getExercises() {
        return exercises;
    }

    public void setExercises(Set<ExerciseSkill> exercises) {
        this.exercises = exercises;
    }

    public void addExercise(ExerciseSkill exercise) {
        exercises.add(exercise);
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }
    
    public double getMastery() {
        return 90;
    }
    
}
