package entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by win10 on 27.11.2016.
 */

@Entity
@DiscriminatorValue("TEACHER")
public class Teacher extends TeachingStaff {


    public Teacher() {
    }

    public Teacher(String name, String patronymic, String surname, Date birth, String phoneNumber, String address,
                   String login, String password, String mail, String additional, School school, String qualification) throws NoSuchAlgorithmException {
        super(name, patronymic, surname, birth, phoneNumber, address, login, password, mail, additional, school, qualification);
    }

    public Teacher(String name, String patronymic, String surname, Date birth, String phoneNumber, String address,
                   String login, String password, String mail, String additional, String qualification) throws NoSuchAlgorithmException {
        super(name, patronymic, surname, birth, phoneNumber, address, login, password, mail, additional, qualification);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "personId=" + getPersonId() +
                ", name='" + getName() + '\'' +
                ", patronymic='" + getPatronymic() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", birth=" + getBirth() +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", login='" + getLogin() + '\'' +
                ", qualification=" + getQualification() +
                ", school=" + getSchool() +
                "}";
    }

}
