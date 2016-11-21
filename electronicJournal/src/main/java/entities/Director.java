package entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.io.Serializable;

/**
 * Created by win10 on 21.11.2016.
 */

@Entity
@DiscriminatorValue("director")
public class Director extends Teacher implements Serializable {

}
