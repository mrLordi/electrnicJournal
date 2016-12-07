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
    private int disciplineId;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    public Discipline() {
    }

    public Discipline(String name) {
        this.name = name;
    }

    public int getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(int disciplineId) {
        this.disciplineId = disciplineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Discipline that = (Discipline) o;

        if (getDisciplineId() != that.getDisciplineId()) return false;
        return getName() != null ? getName().equals(that.getName()) : that.getName() == null;

    }

    @Override
    public int hashCode() {
        int result = getDisciplineId();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Discipline{" +
                "disciplineId=" + disciplineId +
                ", name='" + name + '\'' +
                '}';
    }
}
