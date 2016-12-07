package entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by win10 on 21.11.2016.
 */

@Entity
@DiscriminatorValue("CLASS_TEACHER")
public class ClassTeacher extends TeachingStaff implements Serializable {

    @OneToOne()
    @JoinColumn(name = "class_id")
    private ClassName className;

    public ClassName getClassName() {
        return className;
    }

    public void setClassName(ClassName className) {
        this.className = className;
    }

    public ClassTeacher() {
    }

    public ClassTeacher(String name, String patronymic, String surname, Date birth, String phoneNumber, String address,
                        String login, String password, String mail, String additional, School school, String qualification,
                        ClassName className) throws NoSuchAlgorithmException {
        super(name, patronymic, surname, birth, phoneNumber, address, login, password, mail, additional, school, qualification);
        this.className = className;
    }

    public ClassTeacher(String name, String patronymic, String surname, Date birth, String phoneNumber, String address,
                        String login, String password, String mail, String additional,
                        String qualification, ClassName className) throws NoSuchAlgorithmException{
        super(name, patronymic, surname, birth, phoneNumber, address, login, password, mail, additional, qualification);
        this.className = className;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ClassTeacher that = (ClassTeacher) o;

        return getClassName() != null ? getClassName().equals(that.getClassName()) : that.getClassName() == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getClassName() != null ? getClassName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClassTeacher{" +
                "personId=" + getPersonId() +
                ", name='" + getName() + '\'' +
                ", patronymic='" + getPatronymic() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", birth=" + getBirth() +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", login='" + getLogin() + '\'' +
                ", school=" + getSchool() +
                ", className=" + (className != null ? className.getName() : "none") +
                ", qualification='" + getQualification() + '\'' +
                '}';
    }
}
