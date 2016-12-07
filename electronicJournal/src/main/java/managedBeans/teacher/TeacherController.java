package managedBeans.teacher;

import beans.TeacherBean;
import entities.Director;
import entities.School;
import entities.Teacher;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by win10 on 06.12.2016.
 */

@ManagedBean(name = "teacherController", eager = true)
@SessionScoped
public class TeacherController {

    private final static Logger logger = Logger.getLogger(TeacherController.class);

    @EJB
    private TeacherBean teacherBean;
    private Teacher teacher;
    private School school;
    private Director director;

    private String getCurrentUser() {
        return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }

    public Teacher getTeacher() {
        teacher = teacher == null ? teacherBean.getByLogin(getCurrentUser()) : teacher;
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getBirth() {
        Calendar birth = Calendar.getInstance(Locale.ROOT);
        birth.setTime(teacher.getBirth());
        return birth.get(Calendar.DATE) + "-" + (birth.get(Calendar.MONTH) + 1) + "-" + birth.get(Calendar.YEAR);
    }

    public boolean isDisciplinesExist() {
        return teacher.getDisciplines() != null && !teacher.getDisciplines().isEmpty();
    }

    public boolean isLessonsExist() {
        return teacher.getClasses() != null && !teacher.getClasses().isEmpty();
    }

    public School getSchool() {
        return school == null ? teacher.getSchool() : school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Director getDirector() {
        return director == null ? getSchool().getDirector() : director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public String getDirectorBirth() {
        Calendar date = Calendar.getInstance(Locale.ROOT);
        date.setTime(getDirector().getBirth());
        return date.get(Calendar.DATE) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.YEAR);
    }

    public String toSchoolPage() {
        return "/teacher/school?faces-redirect=true";
    }
}
