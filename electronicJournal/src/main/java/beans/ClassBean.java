package beans;

import entities.ClassName;
import entities.School;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by win10 on 29.11.2016.
 */

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class ClassBean extends Bean<ClassName> {

    public List<ClassName> getAllBySchoolId(School school) {
        TypedQuery<ClassName> query =
                entityManager.createQuery("SELECT c FROM ClassName c WHERE c.school = :school", ClassName.class);

        logger.info(getClass().getName() + ":: all classes with school (" + school + ") were received.");

        return query.setParameter("school", school).getResultList();
    }

    public ClassName getByName(String className) {
        TypedQuery<ClassName> query = entityManager.
                createQuery("SELECT c FROM ClassName c WHERE c.name = :className", ClassName.class);

        ClassName clazz = null;
        try {
            clazz = query.setParameter("className", className).getSingleResult();
            logger.info(getClass().getName() + ":: class  (name=" + className + ") was received. Entity: " + clazz);
        } catch (NoResultException e) {
            logger.warn("Class with name '" + className + "' does not exist!");
        }

        return clazz;
    }
}
