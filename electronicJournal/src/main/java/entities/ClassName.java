package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by win10 on 21.11.2016.
 */

@Entity
@Table(name = "class")
public class ClassName implements Serializable {

    @Id
    @Column(name = "class_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int classId;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToOne(mappedBy = "className")
    private ClassTeacher classTeacher;

    @OneToMany(mappedBy = "className", fetch = FetchType.EAGER)
    private Set<Student> students;

    @OneToMany(mappedBy = "className", fetch = FetchType.EAGER)
    private Set<Lesson> lessons;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    public ClassName() {
    }

    public ClassName(String name, School school) {
        this.name = name;
        this.school = school;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassTeacher getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(ClassTeacher classTeacher) {
        this.classTeacher = classTeacher;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Set<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassName className = (ClassName) o;

        if (getClassId() != className.getClassId()) return false;

        return getName() != null ? getName().equals(className.getName()) : className.getName() == null;

    }

    @Override
    public int hashCode() {
        int result = getClassId();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClassName{" +
                "classId=" + classId +
                ", name='" + name + '\'' +
                '}';
    }
}
