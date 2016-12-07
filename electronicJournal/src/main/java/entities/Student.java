package entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by win10 on 21.11.2016.
 */


@Entity
@DiscriminatorValue("STUDENT")
public class Student extends Person implements Serializable {

    @ManyToOne()
    @JoinColumn(name = "class_id")
    private ClassName className;

    public Student() {
    }

    public Student(String name, String patronymic, String surname, Date birth, String phoneNumber, String address,
                   String login, String password, String mail, String additional, School school, ClassName className)
            throws NoSuchAlgorithmException {
        super(name, patronymic, surname, birth, phoneNumber, address, login, password, mail, additional, school);
        this.className = className;
    }

    public ClassName getClassName() {
        return className;
    }

    public void setClassName(ClassName className) {
        this.className = className;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Student student = (Student) o;

        return getClassName() != null ? getClassName().equals(student.getClassName()) : student.getClassName() == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getClassName() != null ? getClassName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "personId=" + getPersonId() +
                ", name='" + getName() + '\'' +
                ", patronymic='" + getPatronymic() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", birth=" + getBirth() +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", mail='" + getMail() + '\'' +
                ", additional='" + (getAdditional() != null ? getAdditional() : "-") + '\'' +
                ", login='" + getLogin() + '\'' +
                ", school=" + (getSchool() != null ? getSchool() : "none") +
                ", className=" + (className != null ? className.getName() : "none") +
                '}';
    }
}
