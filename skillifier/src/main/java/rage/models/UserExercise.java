package rage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "User_Exercise")
public class UserExercise implements Serializable {


    public UserExercise(User user, Exercise exercise, boolean attempted, boolean completed) {
        this.user = user;
        this.exercise = exercise;
        this.attempted = attempted;
        this.completed = completed;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @JsonIgnore private boolean attempted = false;
    @JsonIgnore private boolean completed = false;
    @JsonIgnore private boolean returnable = true;
    
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "exercise")
    private Exercise exercise;
    
    // Getters and Setters
    
    @JsonIgnore
    public User getUser() {
        return user;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setAttempted(boolean attempted) {
        this.attempted = attempted;
    }

    public boolean isAttempted() {
        return attempted;
    }

    public boolean isCompleted() {
        return completed;
    }

//    public UserExercise createAndReturn() {
//        this.exercise.setAvailableAndReturn(false);
//        return this;
//    }
//
//    public static UserExercise withUnavailableExercise() {
//        new UserExercise()
//    }

    public boolean isReturnable() {
        return returnable;
    }


    public void setReturnable(boolean returnable) {
        this.returnable = returnable;
    }
}
