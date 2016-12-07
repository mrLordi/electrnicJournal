package entities;

import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

/**
 * Created by win10 on 20.11.2016.
 */


@Entity
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorOptions(force = true)
@DiscriminatorColumn(name = "discriminator")
public abstract class Person implements Serializable {

    @Id
    @Column(name = "person_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "patronymic", nullable = false)
    private String patronymic;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "birth", nullable = false)
    private Date birth;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "mail", nullable = false, unique = true)
    private String mail;

    @Column(name = "additional")
    private String additional;

    @ManyToOne()
    @JoinColumn(name = "school_id")
    private School school;

    public Person() {
    }

    public Person(String name, String patronymic, String surname, Date birth, String phoneNumber, String address,
                  String login, String password, String mail, String additional, School school) throws NoSuchAlgorithmException {
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.login = login;
        setPassword(password);
        this.mail = mail;
        this.additional = additional;
        this.school = school;
    }

    public Person(String name, String patronymic, String surname, Date birth, String phoneNumber, String address,
                  String login, String password, String mail, String additional) throws NoSuchAlgorithmException {
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.login = login;
        setPassword(password);
        this.mail = mail;
        this.additional = additional;
    }

    @SuppressWarnings("Since15")
    private static String getEncodedPassword(String password) throws NoSuchAlgorithmException {
        return new String(Base64.getEncoder().encode(encryption(password)));
    }

    private static byte[] encryption(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        return md.digest();
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws NoSuchAlgorithmException {
        this.password = getEncodedPassword(password);
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;

        if (getPersonId() != person.getPersonId()) return false;
        if (getName() != null ? !getName().equals(person.getName()) : person.getName() != null) return false;
        if (getPatronymic() != null ? !getPatronymic().equals(person.getPatronymic()) : person.getPatronymic() != null)
            return false;
        if (getSurname() != null ? !getSurname().equals(person.getSurname()) : person.getSurname() != null)
            return false;
        if (getBirth() != null ? !getBirth().equals(person.getBirth()) : person.getBirth() != null) return false;
        if (getPhoneNumber() != null ? !getPhoneNumber().equals(person.getPhoneNumber()) : person.getPhoneNumber() != null)
            return false;
        if (getAddress() != null ? !getAddress().equals(person.getAddress()) : person.getAddress() != null)
            return false;
        if (getLogin() != null ? !getLogin().equals(person.getLogin()) : person.getLogin() != null) return false;
        if (getMail() != null ? !getMail().equals(person.getMail()) : person.getMail() != null) return false;
        if (getAdditional() != null ? !getAdditional().equals(person.getAdditional()) : person.getAdditional() != null)
            return false;
        return getPassword() != null ? getPassword().equals(person.getPassword()) : person.getPassword() == null;

    }

    @Override
    public int hashCode() {
        int result = getPersonId();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getPatronymic() != null ? getPatronymic().hashCode() : 0);
        result = 31 * result + (getSurname() != null ? getSurname().hashCode() : 0);
        result = 31 * result + (getBirth() != null ? getBirth().hashCode() : 0);
        result = 31 * result + (getPhoneNumber() != null ? getPhoneNumber().hashCode() : 0);
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        result = 31 * result + (getLogin() != null ? getLogin().hashCode() : 0);
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        result = 31 * result + (getMail() != null ? getMail().hashCode() : 0);
        result = 31 * result + (getAdditional() != null ? getAdditional().hashCode() : 0);
        return result;
    }
}
