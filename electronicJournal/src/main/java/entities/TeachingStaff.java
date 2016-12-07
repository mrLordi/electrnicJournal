package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Set;

/**
 * Created by win10 on 21.11.2016.
 */

@MappedSuperclass
public abstract class TeachingStaff extends Person implements Serializable {

    @Column(name = "qualification")
    private String qualification;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "teacher_discipline",
            joinColumns = {@JoinColumn(name = "person_id", referencedColumnName = "person_id")},
            inverseJoinColumns = {@JoinColumn(name = "discipline_id", referencedColumnName = "discipline_id")})
    private Set<Discipline> disciplines;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
    private Set<DisciplineForClass> classes;

    public TeachingStaff() {}

    public TeachingStaff(String name, String patronymic, String surname, Date birth, String phoneNumber, String address,
                         String login, String password, String mail, String additional, School school, String qualification) throws NoSuchAlgorithmException {
        super(name, patronymic, surname, birth, phoneNumber, address, login, password, mail, additional, school);
        this.qualification = qualification;
    }

    public TeachingStaff(String name, String patronymic, String surname, Date birth, String phoneNumber, String address,
                         String login, String password, String mail, String additional, String qualification) throws NoSuchAlgorithmException {
        super(name, patronymic, surname, birth, phoneNumber, address, login, password, mail, additional);
        this.qualification = qualification;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Set<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(Set<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    public Set<DisciplineForClass> getClasses() {
        return classes;
    }

    public void setClasses(Set<DisciplineForClass> classes) {
        this.classes = classes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeachingStaff)) return false;
        if (!super.equals(o)) return false;

        TeachingStaff teacher = (TeachingStaff) o;

        return getQualification() != null ? getQualification().equals(teacher.getQualification()) : teacher.getQualification() == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getQualification() != null ? getQualification().hashCode() : 0);
        return result;
    }
}
