package entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by win10 on 21.11.2016.
 */

@Entity
@DiscriminatorValue("DIRECTOR")
public class Director extends TeachingStaff implements Serializable {

    public Director() {
    }

    public Director(String name, String patronymic, String surname, Date birth, String phoneNumber, String address,
                    String login, String password, String mail, String additional,
                    School school, String qualification) throws NoSuchAlgorithmException {
        super(name, patronymic, surname, birth, phoneNumber, address, login, password, mail, additional, school, qualification);
    }

    public Director(String name, String patronymic, String surname, Date birth, String phoneNumber, String address,
                    String login, String password, String mail,
                    String additional, String qualification) throws NoSuchAlgorithmException{
        super(name, patronymic, surname, birth, phoneNumber, address, login, password, mail, additional, qualification);
    }

    @Override
    public String toString() {
        return "Director{" +
                "personId=" + getPersonId() +
                ", name='" + getName() + '\'' +
                ", patronymic='" + getPatronymic() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", birth=" + getBirth() +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", qualification=" + (getQualification() != null ? getQualification() : "null") +
                ", login='" + getLogin() + '\'' +
                ", school=" + getSchool() +
                "}";
    }
}
