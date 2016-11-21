package entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by win10 on 21.11.2016.
 */

@Entity
@DiscriminatorValue("classTeacher")
public class ClassTeacher extends Teacher implements Serializable {

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "class_id", unique = true, nullable = false)
    private ClassName className;

}
