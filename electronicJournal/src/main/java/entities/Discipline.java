package entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by win10 on 21.11.2016.
 */


@Entity
@Table(name = "disciplines")
public class Discipline implements Serializable {

    @Id
    @Column(name = "discipline_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int classId;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

}
