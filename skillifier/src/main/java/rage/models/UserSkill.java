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
@Table(name = "User_Skill")
public class UserSkill implements Serializable {
    
    public UserSkill(User user, Skill skill, double percentage) {
        this.user = user;
        this.skill = skill;
        this.percentage = percentage;
    }

    // Required by Hibernate
    @SuppressWarnings("initialization.fields.uninitialized")
    private UserSkill() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private double percentage = 0.0;
    
    // Bad naming strategy, refactor
    
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "skill")
    private Skill skill;
    
    // Getters and Setters
    
    public void addSureness(int percentage) {
        this.percentage += percentage;
    }


    public double getPercentage() {
        return percentage;
    }

    public Skill getSkill() {
        return skill;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }


}
