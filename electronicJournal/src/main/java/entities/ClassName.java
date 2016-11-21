package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by win10 on 21.11.2016.
 */

@Entity
@Table(name = "class")
public class ClassName implements Serializable{

    @Id
    @Column(name = "class_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int classId;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToOne(mappedBy = "className")
    private ClassTeacher classTeacher;

    @OneToMany(mappedBy = "className")
    private List<Student> students;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;
}
