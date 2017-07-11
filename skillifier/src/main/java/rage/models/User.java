package rage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "User")
@SuppressWarnings("nullness")
public class User implements Serializable {
    
    // TODO: refactor name, SQL doesn't
    // necessarily like "User"
    
    public User() { }
    
    public User(String username) {
        this.username = username;
    }
    
    // Fields
    
    @Id
    private String username;
    
    private Optional<UserExercise> assignedExercise;
    
    @OneToMany(mappedBy = "skill")
    private Set<UserSkill> skills;
    
    @OneToMany(mappedBy = "exercise")
    private Set<UserExercise> exercises;
    
    // Getters and Setters
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    @JsonIgnore
    public Set<UserSkill> getSkills() {
        return skills;
    }
    
    public void setSkills(Set<UserSkill> skills) {
        this.skills = skills;
    }
    
    public void addExercise(Exercise exercise) {
        exercises.add(new UserExercise(this, exercise, true, true));
    }
    
    public Optional<UserExercise> getAssignedExercise() {
        return assignedExercise;
    }
    
    public void setAssignedExercise(UserExercise exercise) {
        this.assignedExercise = Optional.of(exercise);
    }
    
}
