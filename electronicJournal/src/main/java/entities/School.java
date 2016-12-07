package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by win10 on 21.11.2016.
 */

@Entity
@Table(name = "school")
public class School implements Serializable {

    @Id
    @Column(name = "school_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int schoolId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "information")
    private String information;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "director_id", unique = true, nullable = false)
    private Director director;

    @OneToMany(mappedBy = "school", fetch = FetchType.EAGER)
    private Set<ClassName> classNames;

    @OneToMany(mappedBy = "school")
    private Set<Person> persons;

    public School() {
    }

    public School(String name, String phoneNumber, String address, String information, Director director) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.information = information;
        this.director = director;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public Set<ClassName> getClassNames() {
        return classNames;
    }

    public void setClassNames(Set<ClassName> classNames) {
        this.classNames = classNames;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        School school = (School) o;

        if (getSchoolId() != school.getSchoolId()) return false;
        if (getName() != null ? !getName().equals(school.getName()) : school.getName() != null) return false;
        if (getPhoneNumber() != null ? !getPhoneNumber().equals(school.getPhoneNumber()) : school.getPhoneNumber() != null)
            return false;
        if (getAddress() != null ? !getAddress().equals(school.getAddress()) : school.getAddress() != null)
            return false;
        return getInformation() != null ? getInformation().equals(school.getInformation()) : school.getInformation() == null;

    }

    @Override
    public int hashCode() {
        int result = getSchoolId();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getPhoneNumber() != null ? getPhoneNumber().hashCode() : 0);
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        result = 31 * result + (getInformation() != null ? getInformation().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "School{" +
                "schoolId=" + schoolId +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", information='" + information + '\'' +
                ", director=" + director.getName() + ' ' + director.getSurname() +
                '}';
    }
}
