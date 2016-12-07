package managedBeans.director;

import beans.ClassBean;
import beans.LessonBean;
import beans.LessonResultBean;
import beans.StudentBean;
import entities.*;
import org.apache.log4j.Logger;
import utils.Helper;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by win10 on 04.12.2016.
 */

@ManagedBean(name = "disciplineBean", eager = true)
@SessionScoped
public class DisciplineController {

    private final static Logger logger = Logger.getLogger(DisciplineController.class);

    @EJB
    private StudentBean studentBean;
    @EJB
    private ClassBean classBean;
    @EJB
    private LessonBean lessonBean;
    @EJB
    private LessonResultBean lessonResultBean;

    private DisciplineForClass disciplineForClass;
    private boolean addLesson;
    private boolean editLessonResult;
    private boolean editLesson;

    private Date date;
    private String theme;
    private List<LessonResult> lessonResults;
    private Student student;
    private Lesson lesson;
    private LessonResult lessonResult;
    private List<LessonResult> lessonResultsForEdit = new ArrayList<>();
    private String lessonDate;
    private List<Lesson> lessons;


    private void init() {
        addLesson = false;
        editLessonResult = false;
        editLesson = false;
    }

    public String toSendMessage() {
        init();
        return "/director/sendMessage?faces-redirect=true";
    }

    public String toSchoolPage() {
        init();

        return "/director/school?faces-redirect=true";
    }

    public String toTeacherSchoolPage() {
        init();

        return "/teacher/school?faces-redirect=true";
    }

    public String toTeachersPage() {
        init();

        return "/director/teachers/teachers?faces-redirect=true";
    }

    public String toClassesPage() {
        init();

        return "/director/classes/classes?faces-redirect=true";
    }

    public String toHomePage() {
        init();

        return "/director/directorIndex?faces-redirect=true";
    }

    public String toTeacherHomePage() {
        init();

        return "/teacher/teacherIndex?faces-redirect=true";
    }

    public String toEditLesson() {
        init();
        setLessonResultsForEdit(new ArrayList<LessonResult>());

        editLesson = true;
        return "/discipline/lessonDetails";
    }

    public String toLessonDetails(Lesson lesson) {
        init();
        setLesson(lesson);
        return "/discipline/lessonDetails";
    }

    public LessonResult receiveLessonResult(Lesson lesson) {
        setLessonResult(lessonResultBean.getByStudentAndLesson(student, lesson));
        return getLessonResult();
    }

    public LessonResult receiveLessonResult(Student student) {
        setLessonResult(lessonResultBean.getByStudentAndLesson(student, lesson));
        lessonResultsForEdit.add(getLessonResult());
        return getLessonResult();
    }

    public String toEditLessonResult(Lesson lesson) {
        init();
        setLesson(lesson);
        receiveLessonResult(lesson);
        editLessonResult = true;
        return "/discipline/studentDetails";
    }

    public String cancelEditLessonResult() {

        editLessonResult = false;
        return "/discipline/studentDetails";
    }

    public String editLessonResult() {

        lessonResultBean.update(getLessonResult());

        editLessonResult = false;
        return "/discipline/studentDetails";
    }

    public String toStudentDetails(Student student) {
        init();
        setStudent(student);
        return "/discipline/studentDetails";
    }

    public String toAddLesson() {
        addLesson = true;

        date = null;
        theme = "";
        lessonResults = new ArrayList<>();
        List<Student> students = studentBean.getAllByClass(disciplineForClass.getClassName());
        for (Student student : students) {
            LessonResult lessonResult = new LessonResult();
            lessonResult.setStudent(student);
            lessonResults.add(lessonResult);
        }

        return "/discipline/addLesson?faces-redirect=true";
    }

    public String cancelEditLesson() {
        editLesson = false;
        return "/discipline/disciplineDetails?faces-redirect=true";
    }

    public String editLesson() throws ParseException {
        for (LessonResult lessonResult : lessonResultsForEdit) {
            lessonResultBean.update(lessonResult);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        lesson.setDate(simpleDateFormat.parse(lessonDate));
        lessonBean.update(lesson);

        editLesson = false;
        return "/discipline/disciplineDetails?faces-redirect=true";
    }

    public String getLessonDate() {
        Calendar birth = Calendar.getInstance(Locale.ROOT);
        birth.setTime(lesson.getDate());
        return birth.get(Calendar.DATE) + "-" + (birth.get(Calendar.MONTH) + 1) + "-" + birth.get(Calendar.YEAR);
    }

    public String addLesson() {

        Lesson lesson = new Lesson();
        lesson.setDiscipline(disciplineForClass.getDiscipline());
        lesson.setTeacher(disciplineForClass.getTeacher());
        lesson.setClassName(disciplineForClass.getClassName());
        lesson.setDate(date);
        lesson.setTheme(theme);
        lesson.setLessonResults(lessonResults);
        Lesson lesson1 = lessonBean.add(lesson);

        for (LessonResult lessonResult : lessonResults) {
            lessonResult.setLesson(lesson1);
            lessonResultBean.add(lessonResult);
        }

        ClassName className = disciplineForClass.getClassName();
        className.getLessons().add(lesson1);
        classBean.update(className);

        addLesson = false;
        return "/discipline/disciplineDetails?faces-redirect=true";
    }

    public String cancelAddLesson() {
        addLesson = false;
        return "/discipline/disciplineDetails?faces-redirect=true";
    }

    public String disciplineForClassDetails(DisciplineForClass disciplineForClass) {
        setDisciplineForClass(disciplineForClass);
        return "/discipline/disciplineDetails?faces-redirect=true";
    }

    public List<Student> getAllStudents() {
        return studentBean.getAllByClass(getDisciplineForClass().getClassName());
    }

    public List<Lesson> getAllLessons() {
        lessons = lessonBean.getAllByClassAndDiscipline(disciplineForClass.getClassName(),
                disciplineForClass.getDiscipline());
        return lessons;
    }

    public String dateOfLesson(Lesson lesson) {
        Calendar birth = Calendar.getInstance(Locale.ROOT);
        birth.setTime(lesson.getDate());
        return birth.get(Calendar.DATE) + "-" + (birth.get(Calendar.MONTH) + 1) + "-" + birth.get(Calendar.YEAR);
    }

//    public boolean isLessonExist() {
//        Set<Lesson> lessons = getDisciplineForClass().getClassName().getLessons();
//        return lessons != null && !lessons.isEmpty();
//    }

    public boolean isStudentExist() {
        Set<Student> students = getDisciplineForClass().getClassName().getStudents();
        return students != null && !students.isEmpty();
    }

    public void validateForm(ComponentSystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();

        String theme = Helper.getInformation("theme", components);

        if (theme.isEmpty()) {
            FacesMessage message = new FacesMessage("Fill in 'theme' field!");
            logger.warn("Required field 'theme' must be filled!");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(components.getClientId(), message);
            facesContext.renderResponse();
        }

    }

    public boolean isDirectorRole() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .isUserInRole("DIRECTOR");
    }

    public boolean isTeacherRole() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .isUserInRole("TEACHER");
    }

    public boolean isClassTeacherRole() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .isUserInRole("CLASS_TEACHER");
    }

    public DisciplineForClass getDisciplineForClass() {
        return disciplineForClass;
    }

    public void setDisciplineForClass(DisciplineForClass disciplineForClass) {
        this.disciplineForClass = disciplineForClass;
    }

    public boolean isAddLesson() {
        return addLesson;
    }

    public void setAddLesson(boolean addLesson) {
        this.addLesson = addLesson;
    }

    public boolean isEditLessonResult() {
        return editLessonResult;
    }

    public void setEditLessonResult(boolean editLessonResult) {
        this.editLessonResult = editLessonResult;
    }

    public boolean isEditLesson() {
        return editLesson;
    }

    public void setEditLesson(boolean editLesson) {
        this.editLesson = editLesson;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public List<LessonResult> getLessonResults() {
        return lessonResults;
    }

    public void setLessonResults(List<LessonResult> lessonResults) {
        this.lessonResults = lessonResults;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public LessonResult getLessonResult() {
        return lessonResult;
    }

    public void setLessonResult(LessonResult lessonResult) {
        this.lessonResult = lessonResult;
    }

    public List<LessonResult> getLessonResultsForEdit() {
        return lessonResultsForEdit;
    }

    public void setLessonResultsForEdit(List<LessonResult> lessonResultsForEdit) {
        this.lessonResultsForEdit = lessonResultsForEdit;
    }

    public void setLessonDate(String lessonDate) {
        this.lessonDate = lessonDate;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
