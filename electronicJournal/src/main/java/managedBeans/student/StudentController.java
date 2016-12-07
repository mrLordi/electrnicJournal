package managedBeans.student;

import beans.DisciplineForClassBean;
import beans.LessonBean;
import beans.LessonResultBean;
import beans.StudentBean;
import entities.*;
import org.apache.log4j.Logger;
import utils.Helper;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.*;

/**
 * Created by win10 on 06.12.2016.
 */

@ManagedBean(name = "studentController", eager = true)
@SessionScoped
public class StudentController {

    private final static Logger logger = Logger.getLogger(StudentController.class);

    @EJB
    private StudentBean studentBean;
    @EJB
    private DisciplineForClassBean disciplineForClassBean;
    @EJB
    private LessonBean lessonBean;
    @EJB
    private LessonResultBean lessonResultBean;

    private Student student;
    private Discipline discipline;
    private LessonResult lessonResult;
    private TeachingStaff teacher;
    private List<Lesson> lessons;
    private ClassTeacher classTeacher;
    private School school;
    private Director director;
    private boolean teachersPage;

    public boolean isClassTeacherExist() {
        return getClassTeacher() != null;
    }

    private void init() {
        teachersPage = false;
    }

    public String toClassTeacher() {
        init();
        return "/student/classTeacherPage?faces-redirect=true";
    }

    public String toTeacherDetails() {
        init();
        return "/student/teacherDetails?faces-redirect=true";
    }

    public String toTeacherDetails(TeachingStaff teacher) {
        setTeacher(teacher);
        teachersPage = true;
        return "/student/teacherDetails?faces-redirect=true";
    }

    public String toDetailsOfLesson(Discipline discipline, Person teacher) {
        setDiscipline(discipline);
        setTeacher((TeachingStaff) teacher);
        setLessons(lessonBean.getAllByClassAndDiscipline(getStudent().getClassName(), discipline));
        return "/student/disciplineDetails?faces-redirect=true";
    }

    public String dateOfLesson(Lesson lesson) {
        Calendar date = Calendar.getInstance(Locale.ROOT);
        date.setTime(lesson.getDate());
        return date.get(Calendar.DATE) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.YEAR);
    }

    public String getTeacherBirth() {
        Calendar date = Calendar.getInstance(Locale.ROOT);
        date.setTime(getTeacher().getBirth());
        return date.get(Calendar.DATE) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.YEAR);
    }

    public String getClassTeacherBirth() {
        Calendar date = Calendar.getInstance(Locale.ROOT);
        date.setTime(getClassTeacher().getBirth());
        return date.get(Calendar.DATE) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.YEAR);
    }

    public String getDirectorBirth() {
        Calendar date = Calendar.getInstance(Locale.ROOT);
        date.setTime(getDirector().getBirth());
        return date.get(Calendar.DATE) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.YEAR);
    }

    public LessonResult receiveLessonResult(Lesson lesson) {
        setLessonResult(lessonResultBean.getByStudentAndLesson(getStudent(), lesson));
        return getLessonResult();
    }

    public String toSchoolPage() {
        init();
        return "/student/school?faces-redirect=true";
    }

    public String toHomePage() {
        init();
        return "/student/studentIndex?faces-redirect=true";
    }

    public String toTeachersPage() {
        init();
        return "/student/teachers?faces-redirect=true";
    }

    public List<DisciplineForClass> getAllDisciplinesForClass() {
        return disciplineForClassBean.getAllDisciplinesForClass(getStudent().getClassName());
    }

    public Set<TeachingStaff> getAllTeachers() {
        List<DisciplineForClass> forClasses = getAllDisciplinesForClass();
        Set<TeachingStaff> result = new HashSet<>();

        for (DisciplineForClass forClass : forClasses) {
            result.add((TeachingStaff) forClass.getTeacher());
        }

        return result;
    }

    public Student getStudent() {
        student = student == null ? studentBean.getByLogin(Helper.getCurrentUser()) : student;
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getBirth() {
        Calendar birth = Calendar.getInstance(Locale.ROOT);
        birth.setTime(getStudent().getBirth());
        return birth.get(Calendar.DATE) + "-" + (birth.get(Calendar.MONTH) + 1) + "-" + birth.get(Calendar.YEAR);
    }

    public ClassTeacher getClassTeacher() {
        return classTeacher == null ? student.getClassName().getClassTeacher() : classTeacher;
    }

    public School getSchool() {
        return school == null ? student.getSchool() : school;
    }

    public Director getDirector() {
        return director == null ? getSchool().getDirector() : director;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public LessonResult getLessonResult() {
        return lessonResult;
    }

    public void setLessonResult(LessonResult lessonResult) {
        this.lessonResult = lessonResult;
    }

    public TeachingStaff getTeacher() {
        return teacher;
    }

    public void setTeacher(TeachingStaff teacher) {
        this.teacher = teacher;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void setClassTeacher(ClassTeacher classTeacher) {
        this.classTeacher = classTeacher;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public boolean isTeachersPage() {
        return teachersPage;
    }

    public void setTeachersPage(boolean teachersPage) {
        this.teachersPage = teachersPage;
    }
}
