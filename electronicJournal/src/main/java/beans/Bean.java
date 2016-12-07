package beans;

import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by win10 on 21.11.2016.
 */
public class Bean<T> {

    protected final static Logger logger = Logger.getLogger(Bean.class);

    @PersistenceContext(unitName = "electronicJournal")
    protected EntityManager entityManager;

    public T add(T entity) {
        return entityManager.merge(entity);
    }

    public void update(T entity) {

        try {
            entityManager.merge(entity);
        } catch (Exception e) {
            logger.error("An error occurred while updating an entity(" + entity + "). Error: " + e.getMessage());
            throw e;
        }

        logger.info("Entity(" + entity + ") was updated.");
    }

    public void delete(T entity) {
        entityManager.remove(entityManager.merge(entity));
        logger.info("Entity(" + entity + ") was deleted.");
    }

}
