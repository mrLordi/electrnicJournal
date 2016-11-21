package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by win10 on 21.11.2016.
 */

@Entity
@Table(name = "school")
public class School implements Serializable {

    @Id
    @Column(name = "school_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int schoolId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "information")
    private String information;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "director_id", unique = true, nullable = false)
    private Director director;

    @OneToMany(mappedBy = "school")
    private List<ClassName> classNames;

    @OneToMany(mappedBy = "school")
    private List<Person> persons;

}
