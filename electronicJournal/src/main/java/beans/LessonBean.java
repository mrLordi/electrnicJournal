package beans;

import entities.ClassName;
import entities.Discipline;
import entities.Lesson;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by win10 on 04.12.2016.
 */

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class LessonBean extends Bean<Lesson> {

    public List<Lesson> getAllByClassAndDiscipline(ClassName className, Discipline discipline) {
        TypedQuery<Lesson> query =
                entityManager.createQuery(
                        "SELECT l FROM Lesson l WHERE l.className = :className AND l.discipline = :discipline",
                        Lesson.class);
        logger.info(getClass().getName() + ":: all lesson (" + className + ", " + discipline + ") was received.");
        return query.setParameter("className", className).setParameter("discipline", discipline).getResultList();
    }
}
