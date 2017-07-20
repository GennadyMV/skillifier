package rage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Week")
public class Week implements Serializable {

    public Week(Course course) {
        this.course = course;
    }

    // Required by Hibernate
    @SuppressWarnings("initialization.fields.uninitialized")
    private Week() {
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private int weekNumber;
    
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "course")
    private Course course;
    
    @OneToMany(mappedBy = "week", cascade = {CascadeType.ALL})
    private List<Theme> themes = new ArrayList<>();
    
    // Getters and Setters
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public int getWeekNumber() {
        return weekNumber;
    }
    
    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    @JsonIgnore
    public Course getCourse() {
        return course;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }
    
    public List<Theme> getThemes() {
        return themes;
    }
    
    public void setThemes(List<Theme> themes) {
        this.themes = themes;
    }
    
}
