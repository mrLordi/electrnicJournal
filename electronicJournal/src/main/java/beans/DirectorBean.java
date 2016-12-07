package beans;

import entities.Director;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.TypedQuery;

/**
 * Created by win10 on 23.11.2016.
 */
@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class DirectorBean extends Bean<Director> {

    public Director getByLogin(String director) {
        TypedQuery<Director> query = entityManager.
                createQuery("SELECT d FROM Director d WHERE d.login = :person", Director.class);
        Director director1 = query.setParameter("person", director).getSingleResult();
        logger.info(getClass().getName() + ":: director (login=" + director + ") was received. Entity: " + director1);
        return director1;
    }

}
