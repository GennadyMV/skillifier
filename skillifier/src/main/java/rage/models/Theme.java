package rage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Theme")
public class Theme implements Serializable {

    public Theme(String name, Week week) {
        this.name = name;
        this.week = week;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @NotNull
    private String name;
    
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "week")
    private Week week;
    
    @OneToMany(mappedBy = "theme", cascade = {CascadeType.ALL})
    private Set<Skill> skills = new HashSet<>();
    
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
    public Week getWeek() {
        return week;
    }
    
    public void setWeek(Week week) {
        this.week = week;
    }
    
    public Set<Skill> getSkills() {
        return skills;
    }
    
    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }
    
}
