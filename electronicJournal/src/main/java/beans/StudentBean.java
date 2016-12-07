package beans;

import entities.ClassName;
import entities.Student;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by win10 on 29.11.2016.
 */

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class StudentBean extends Bean<Student> {

    public Student getByLogin(String student) {
        TypedQuery<Student> query = entityManager.
                createQuery("SELECT s FROM Student s WHERE s.login = :student", Student.class);
        Student s = query.setParameter("student", student).getSingleResult();
        logger.info(getClass().getName() + ":: student (login=" + student + ") was received. Entity: " + s);
        return s;
    }

    public List<Student> getAllByClass(ClassName className) {
        TypedQuery<Student> query =
                entityManager.createQuery("SELECT s FROM Student s WHERE s.className = :className", Student.class);
        logger.info(getClass().getName() + ":: all students (" + className + ") were received.");
        return query.setParameter("className", className).getResultList();
    }

}
