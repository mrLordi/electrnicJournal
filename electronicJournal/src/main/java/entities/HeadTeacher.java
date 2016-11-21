package entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Created by win10 on 21.11.2016.
 */

@Entity
@DiscriminatorValue("headTeacher")
public class HeadTeacher extends Teacher implements Serializable {



}
