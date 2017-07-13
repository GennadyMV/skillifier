package rage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Course")
public class Course implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @NotNull
    @Column(unique = true)
    private String name = "";
    
    @OneToMany(mappedBy = "course", cascade = {CascadeType.ALL})
    private Set<Week> weeks = new HashSet<>();
    
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
    public Set<Week> getWeeks() {
        return weeks;
    }
    
    public void setWeeks(Set<Week> weeks) {
        this.weeks = weeks;
    }
    
    public void addWeek(Week week) {
        weeks.add(week);
    }
    
}
