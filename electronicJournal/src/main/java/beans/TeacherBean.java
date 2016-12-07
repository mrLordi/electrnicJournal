package beans;

import entities.School;
import entities.Teacher;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by win10 on 28.11.2016.
 */

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class TeacherBean extends Bean<Teacher> {

    public List<Teacher> getAllBySchoolId(School school) {
        TypedQuery<Teacher> query =
                entityManager.createQuery("SELECT t FROM Teacher t WHERE t.school = :school", Teacher.class);
        logger.info(getClass().getName() + ":: all teachers (" + school + ") were received.");
        return query.setParameter("school", school).getResultList();
    }

    public Teacher getByLogin(String teacherLogin) {
        TypedQuery<Teacher> query = entityManager.
                createQuery("SELECT t FROM Teacher t WHERE t.login = :login", Teacher.class);


        Teacher teacher = null;
        try {
            teacher = query.setParameter("login", teacherLogin).getSingleResult();
            logger.info(getClass().getName() + ":: teacher (login=" + teacherLogin + ") was received. Entity: " + teacher);
        } catch (NoResultException e) {
            logger.warn("Teacher with login '" + teacherLogin + "' does not exist!");
        }

        return teacher;
    }
}
