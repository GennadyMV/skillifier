package rage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "User")
public class User implements Serializable {
    
    // TODO: refactor name, SQL doesn't
    // necessarily like "User"
    
    public User(String username) {
        this.username = username;
    }
    
    @Id
    private String username;
    
    @Nullable private UserExercise assignedExercise = null;
    
    @OneToMany(mappedBy = "skill")
    private Set<UserSkill> skills = new HashSet<>();
    
    @OneToMany(mappedBy = "exercise")
    private Set<UserExercise> exercises = new HashSet<>();
    
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
        if (this.assignedExercise == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.assignedExercise);
    }
    
    public void setAssignedExercise(@Nullable UserExercise exercise) {
        this.assignedExercise = exercise;
    }
    
}