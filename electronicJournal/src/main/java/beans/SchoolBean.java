package beans;

import entities.School;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * Created by win10 on 27.11.2016.
 */

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class SchoolBean extends Bean<School> {

}
