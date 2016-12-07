package beans;

import entities.Discipline;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Created by win10 on 28.11.2016.
 */

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class DisciplineBean extends Bean<Discipline> {

    public Discipline getByName(String discipline) {
        TypedQuery<Discipline> query = entityManager.
                createQuery("SELECT d FROM Discipline d WHERE d.name = :discipline", Discipline.class);

        Discipline d = null;
        try {
            d = query.setParameter("discipline", discipline).getSingleResult();
            logger.info(getClass().getName() + ":: discipline (name=" + discipline + ") was received. Entity: " + d);
        } catch (NoResultException e) {
            logger.warn("Discipline with name '" + discipline + "' does not exist!");
        }

        return d;
    }
}
