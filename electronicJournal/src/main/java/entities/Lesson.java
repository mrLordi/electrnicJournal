package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by win10 on 01.12.2016.
 */

@Entity
@Table(name = "lesson")
public class Lesson implements Serializable {

    @Id
    @Column(name = "lesson_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lessonId;

    @Column(name = "theme", nullable = false)
    private String theme;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "discipline_id", nullable = false)
    private Discipline discipline;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "person_id", nullable = false)
    private Person teacher;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassName className;

    @OneToMany(mappedBy = "lesson")
    private List<LessonResult> lessonResults;

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
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

    public List<LessonResult> getLessonResults() {
        return lessonResults;
    }

    public void setLessonResults(List<LessonResult> lessonResults) {
        this.lessonResults = lessonResults;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lesson = (Lesson) o;

        if (getLessonId() != lesson.getLessonId()) return false;
        if (getTheme() != null ? !getTheme().equals(lesson.getTheme()) : lesson.getTheme() != null) return false;
        return getDate() != null ? getDate().equals(lesson.getDate()) : lesson.getDate() == null;

    }

    @Override
    public int hashCode() {
        int result = getLessonId();
        result = 31 * result + (getTheme() != null ? getTheme().hashCode() : 0);
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "lessonId=" + lessonId +
                ", theme='" + theme + '\'' +
                ", date=" + date +
                ", discipline=" + (discipline != null ? discipline.getName() : "-") +
                ", teacher=" + (teacher != null ? teacher.getName() + " " + teacher.getSurname() : "-") +
                '}';
    }
}
