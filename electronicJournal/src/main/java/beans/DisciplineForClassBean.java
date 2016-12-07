package beans;

import entities.ClassName;
import entities.DisciplineForClass;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by win10 on 03.12.2016.
 */

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class DisciplineForClassBean extends Bean<DisciplineForClass> {

    public List<DisciplineForClass> getAllDisciplinesForClass(ClassName className) {
        TypedQuery<DisciplineForClass> query = entityManager.
                createQuery("SELECT dfc FROM DisciplineForClass dfc WHERE dfc.className = :className", DisciplineForClass.class);
        logger.info(getClass().getName() + ":: all disciplines for class (" + className + ") were received.");
        return query.setParameter("className", className).getResultList();
    }

}
