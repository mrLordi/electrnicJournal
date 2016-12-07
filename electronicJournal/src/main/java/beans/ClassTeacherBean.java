package beans;

import entities.ClassTeacher;
import entities.School;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by win10 on 28.11.2016.
 */

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class ClassTeacherBean extends Bean<ClassTeacher> {

    public List<ClassTeacher> getAllBySchoolId(School school) {
        TypedQuery<ClassTeacher> query =
                entityManager.createQuery("SELECT ct FROM ClassTeacher ct WHERE  ct.school = :school", ClassTeacher.class);
        logger.info(getClass().getName() + ":: all class teachers with school (" + school + ") were received.");
        return query.setParameter("school", school).getResultList();
    }

    public ClassTeacher getByLogin(String classTeacherLogin) {
        TypedQuery<ClassTeacher> query = entityManager.
                createQuery("SELECT ct FROM ClassTeacher ct WHERE ct.login = :login", ClassTeacher.class);
        ClassTeacher classTeacher = query.setParameter("login", classTeacherLogin).getSingleResult();
        logger.info(getClass().getName() + ":: class teacher (login=" + classTeacherLogin + ") was received. Entity: "
                + classTeacher);
        return classTeacher;
    }
}
