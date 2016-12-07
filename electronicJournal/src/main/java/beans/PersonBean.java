package beans;

import entities.Person;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Created by win10 on 06.12.2016.
 */

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class PersonBean extends Bean<Person> {

    public Person getByMail(String mail) {
        TypedQuery<Person> query = entityManager.
                createQuery("SELECT p FROM Person p WHERE p.mail = :mail", Person.class);

        Person person = null;
        try {
            person = query.setParameter("mail", mail).getSingleResult();
            logger.info(getClass().getName() + ":: person (e-mail=" + mail + ") was received. Entity: " + person);
        } catch (NoResultException e) {
            logger.warn("Person with e-mail '" + mail + "' does not exist!");
        }

        return person;
    }
}
