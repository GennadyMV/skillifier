package rage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Exercise")
@SuppressWarnings("nullness")
public class Exercise implements Serializable {

    public Exercise() {
    }

    public Exercise(String name) {
        this.name = name;
        skills = new HashSet<>();
    }

    // Fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @Transient
    private boolean available = true;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "skillExercise")
    private Set<ExerciseSkill> skills;

    @OneToMany(mappedBy = "user")
    private Set<UserExercise> users;

    @Transient
    private boolean attempted = false;

    @Transient
    private boolean completed = false;

    @Transient
    private boolean allReviewPointsGiven = false;

    @Transient
    private boolean returnable = true;

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

    @JsonProperty("zip_url")
    public String getZipUrl() {
        return System.getProperty("server.url") + "download";
    }

    @JsonIgnore
    public String getDownloadUrl() {
        return System.getProperty("server.local.download") + name;
    }

    public boolean getAvailable() {
        return available;
    }

    public Exercise setAvailableAndReturn(boolean available) {
        this.skills = new HashSet<>();
        this.available = available;
        return this;
    }

    public boolean isReturnable() {
        return returnable;
    }

    public String getCourseName() {
        // A somewhat ugly fix for the
        // requirement of a Course name
        for (ExerciseSkill skill : skills) {
            return skill.getSkill().getTheme().getWeek().getCourse().getName();
        }
        return "Not assigned";
    }

    public Set<ExerciseSkill> getSkills() {
        return skills;
    }

    public void setSkills(Set<ExerciseSkill> skills) {
        this.skills = skills;
    }

    public void setAttempted(boolean attempted) {
        this.attempted = attempted;
    }

    public boolean isAttempted() {
        return attempted;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setReturnable(boolean returnable) {
        this.returnable = returnable;
    }

    public void setAllReviewPointsGiven(boolean allReviewPointsGiven) {
        this.allReviewPointsGiven = allReviewPointsGiven;
    }

    public boolean isCompleted() {
        return completed;
    }

    @JsonProperty("all_review_points_given")
    public boolean isAllReviewPointsGiven() {
        return allReviewPointsGiven;
    }

    public int getWeekNumber() {
        return skills.stream().findFirst().map((es) -> (es.getSkill().getWeekNumber())).orElse(0);
    }

}
