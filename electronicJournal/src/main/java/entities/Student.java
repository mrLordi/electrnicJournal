package entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by win10 on 21.11.2016.
 */


@Entity
@DiscriminatorValue("student")
public class Student extends Person implements Serializable {

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassName className;

}
