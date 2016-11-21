package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by win10 on 21.11.2016.
 */

@Entity
@DiscriminatorValue("teacher")
public class Teacher extends Person implements Serializable {

    @Column(name = "qualification")
    private String qualification;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "teacher_discipline",
            joinColumns = {@JoinColumn(name = "person_id", referencedColumnName = "person_id")},
            inverseJoinColumns = {@JoinColumn(name = "discipline_id", referencedColumnName = "discipline_id")})
    private List<Discipline> disciplines;


    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "teacher_class",
            joinColumns = {@JoinColumn(name = "person_id", referencedColumnName = "person_id")},
            inverseJoinColumns = {@JoinColumn(name = "class_id", referencedColumnName = "class_id")})
    private List<ClassName> classNames;

}
