package entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by win10 on 02.12.2016.
 */

@Entity
@Table(name = "lesson_result")
public class LessonResult implements Serializable {

    @Id
    @Column(name = "lr_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lrId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "person_id", nullable = false)
    private Student student;

    @Column(name = "additional")
    private String additional;

    @Column(name = "activity")
    private String activity;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    public int getLrId() {
        return lrId;
    }

    public void setLrId(int lrId) {
        this.lrId = lrId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LessonResult that = (LessonResult) o;

        if (getLrId() != that.getLrId()) return false;
        if (getAdditional() != null ? !getAdditional().equals(that.getAdditional()) : that.getAdditional() != null)
            return false;
        return getActivity() != null ? getActivity().equals(that.getActivity()) : that.getActivity() == null;

    }

    @Override
    public int hashCode() {
        int result = getLrId();
        result = 31 * result + (getAdditional() != null ? getAdditional().hashCode() : 0);
        result = 31 * result + (getActivity() != null ? getActivity().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LessonResult{" +
                "lrId=" + lrId +
                ", student=" + student.getName() + " " + student.getSurname() +
                ", additional='" + additional + '\'' +
                ", activity='" + activity + '\'' +
                ", lesson=" + lesson +
                '}';
    }
}
