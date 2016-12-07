package entities;

import javax.persistence.*;

/**
 * Created by win10 on 03.12.2016.
 */

@Entity
@Table(name = "discipline_class")
public class DisciplineForClass {

    @Id
    @Column(name = "dc_Id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dcId;

    @ManyToOne()
    @JoinColumn(name = "person_id", nullable = false)
    private Person teacher;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassName className;

    @ManyToOne()
    @JoinColumn(name = "discipline_id", nullable = false)
    private Discipline discipline;

    public int getDcId() {
        return dcId;
    }

    public void setDcId(int dcId) {
        this.dcId = dcId;
    }

    public Person getTeacher() {
        return teacher;
    }

    public void setTeacher(Person teacher) {
        this.teacher = teacher;
    }

    public ClassName getClassName() {
        return className;
    }

    public void setClassName(ClassName className) {
        this.className = className;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DisciplineForClass that = (DisciplineForClass) o;

        return getDcId() == that.getDcId();

    }

    @Override
    public int hashCode() {
        return getDcId();
    }

    @Override
    public String toString() {
        return "DisciplineForClass{" +
                "dcId=" + dcId +
                ", teacher=" + teacher.getName() + " " + teacher.getSurname() +
                ", className=" + className.getName() +
                ", discipline=" + discipline.getName() +
                '}';
    }
}
