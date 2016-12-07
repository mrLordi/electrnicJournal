package beans;

import entities.Lesson;
import entities.LessonResult;
import entities.Student;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Created by win10 on 04.12.2016.
 */

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class LessonResultBean extends Bean<LessonResult> {

    public LessonResult getByStudentAndLesson(Student student, Lesson lesson) {

        TypedQuery<LessonResult> query =
                entityManager.createQuery(
                        "SELECT lr FROM LessonResult lr WHERE lr.student = :student AND lr.lesson = :lesson",
                        LessonResult.class);

        LessonResult lessonResult = null;
        try {
            lessonResult = query.setParameter("student", student).setParameter("lesson", lesson).getSingleResult();
            logger.info(getClass().getName() + ":: lesson result(" + student + ", " + lesson +
                    ") was received. Entity: " + lessonResult);
        } catch (NoResultException e) {
            logger.warn("Lesson result for " + student + " in lesson " + lesson + " does not exist!");
        }

        if (lessonResult == null) {
            lessonResult = new LessonResult();
            lessonResult.setStudent(student);
            lessonResult.setLesson(lesson);
            lessonResult.setActivity("");
            lessonResult.setAdditional("");

            lessonResult = add(lessonResult);
        }

        return lessonResult;

    }

}
