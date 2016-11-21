package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by win10 on 20.11.2016.
 */


@Entity
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminator")
@MappedSuperclass
public abstract class Person implements Serializable {

    @Id
    @Column(name = "person_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "patronymic", nullable = false)
    private String patronymic;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "birth", nullable = false)
    private Date birth;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

}
