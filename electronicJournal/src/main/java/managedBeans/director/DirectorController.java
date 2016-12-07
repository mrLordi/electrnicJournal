package managedBeans.director;

import beans.ClassBean;
import beans.DirectorBean;
import beans.DisciplineBean;
import beans.DisciplineForClassBean;
import entities.ClassName;
import entities.Director;
import entities.Discipline;
import entities.DisciplineForClass;
import org.apache.log4j.Logger;
import utils.Helper;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by win10 on 25.11.2016.
 */

@ManagedBean(name = "directorBean", eager = true)
@SessionScoped
public class DirectorController {

    private final static Logger logger = Logger.getLogger(DirectorController.class);

    @EJB
    private DirectorBean directorBean;

    @EJB
    private DisciplineBean disciplineBean;

    @EJB
    private ClassBean classBean;

    @EJB
    private DisciplineForClassBean disciplineForClassBean;


    private Director director;
    private boolean editPersonalInformation;
    private boolean editTeacherOpportunities;
    private boolean addQualification;
    private boolean addDiscipline;
    private boolean addLesson;

    private String birth;
    private String qualification;
    private String discipline;
    private String className;

    private void init() {
        addLesson = false;
        editPersonalInformation = false;
        editTeacherOpportunities = false;
        addQualification = false;
        addDiscipline = false;
    }

    public String toSendMessage() {
        init();
        return "/director/sendMessage?faces-redirect=true";
    }

    public String toSchoolPage() {
        init();

        return "/director/school?faces-redirect=true";
    }

    public String toHomePage() {
        init();

        return "/director/directorIndex?faces-redirect=true";
    }

    public String toTeachersPage() {
        init();

        return "/director/teachers/teachers?faces-redirect=true";
    }

    public String toClassesPage() {
        init();

        return "/director/classes/classes?faces-redirect=true";
    }

    public String toAddDiscipline() {
        init();
        addDiscipline = true;
        discipline = "";

        return "/director/directorIndex";
    }

    public String cancelAddDiscipline() {
        addDiscipline = false;
        return "/director/directorIndex";
    }

    public String addDiscipline() {
        Discipline disciplineForAdd = disciplineBean.getByName(discipline);

        if (disciplineForAdd == null) {
            disciplineForAdd = new Discipline(discipline);
            disciplineForAdd = disciplineBean.add(disciplineForAdd);
        }
        director.getDisciplines().add(disciplineForAdd);
        directorBean.update(director);

        addDiscipline = false;
        return "/director/directorIndex";
    }

    public String deleteDiscipline(Discipline discipline) {
        director.getDisciplines().remove(discipline);
        directorBean.update(director);
        return "/director/directorIndex";
    }

    public void validateDiscipline(ComponentSystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();

        String discipline = Helper.getInformation("discipline", components);

        if (discipline.isEmpty()) {
            FacesMessage message = new FacesMessage("Fill in 'discipline' field!");
            logger.warn("Required field 'discipline' must be filled!");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(components.getClientId(), message);
            facesContext.renderResponse();
        }
    }

    public String toAddQualification() {
        init();
        addQualification = true;
        qualification = "";
        return "/director/directorIndex";
    }

    public String toAddLesson() {
        init();
        addLesson = true;
        className = "";
        discipline = "";
        return "/director/directorIndex";
    }

    public void validateQualification(ComponentSystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();

        String qualification = Helper.getInformation("qualification", components);

        if (qualification.isEmpty()) {
            FacesMessage message = new FacesMessage("Fill in 'qualification' field!");
            logger.warn("Required field 'qualification' must be filled!");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(components.getClientId(), message);
            facesContext.renderResponse();
        }
    }

    public void validateLesson(ComponentSystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();

        String discipline = Helper.getInformation("addDiscipline", components);
        String className = Helper.getInformation("class", components);

        FacesMessage message = null;
        if (discipline.isEmpty() || className.isEmpty()) {
            message = new FacesMessage("Fill in all required fields!");
            logger.warn("All required fields must be filled!");
        }

        ClassName clazz = classBean.getByName(className);
        if (clazz == null) {
            message = new FacesMessage("Class with name '" + className + "' does not exist!");
            logger.warn("Class with name '" + className + "' does not exist!");
        }

        if (message != null) {
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(components.getClientId(), message);
            facesContext.renderResponse();
        }

    }


    public String addQualification() {
        director.setQualification(qualification);
        directorBean.update(director);

        addQualification = false;
        return "/director/directorIndex";
    }

    public String cancelAddQualification() {
        addQualification = false;
        return "/director/directorIndex";
    }

    public String cancelAddLesson() {
        addLesson = false;
        return "/director/directorIndex";
    }

    public String addLesson() {
        Discipline disciplineForAdd = disciplineBean.getByName(discipline);

        if (disciplineForAdd == null) {

            disciplineForAdd = new Discipline(discipline);
            Discipline addedDiscipline = disciplineBean.add(disciplineForAdd);

            director.getDisciplines().add(addedDiscipline);
            directorBean.update(director);
        }

        DisciplineForClass disciplineForClass = new DisciplineForClass();
        disciplineForClass.setClassName(classBean.getByName(className));
        disciplineForClass.setTeacher(director);

        disciplineForClass.setDiscipline(disciplineBean.getByName(discipline));

        DisciplineForClass forClass = disciplineForClassBean.add(disciplineForClass);

        director.getClasses().add(forClass);

        addLesson = false;
        return "/director/directorIndex";
    }

    public String toChangeTeacherOpportunities() {
        editTeacherOpportunities = true;
        return "/director/directorIndex";
    }

    public String cancelSaveTeacherOpportunities() {
        editTeacherOpportunities = false;
        return "/director/directorIndex";
    }

    public String saveTeacherOpportunities() {
        directorBean.update(director);

        editTeacherOpportunities = false;
        return "/director/directorIndex";
    }

    public Director getDirector() {
        if (director == null) {
            director = directorBean.getByLogin(Helper.getCurrentUser());
        }

        return director;
    }

    public String getBirth() {
        Calendar birth = Calendar.getInstance(Locale.ROOT);
        birth.setTime(director.getBirth());
        return birth.get(Calendar.DATE) + "-" + (birth.get(Calendar.MONTH) + 1) + "-" + birth.get(Calendar.YEAR);
    }

    public String toChangePersonalInformation() {
        editPersonalInformation = true;
        return "/director/directorIndex";
    }

    public String cancelSavePersonalInformation() {
        editPersonalInformation = false;
        return "/director/directorIndex";
    }

    public String savePersonalInformation() throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        director.setBirth(simpleDateFormat.parse(birth));
        directorBean.update(director);

        editPersonalInformation = false;
        return "/director/directorIndex";
    }

    public boolean isSchoolExist() {
        return getDirector().getSchool() != null;
    }

    public boolean isQualificationExist() {
        return director.getQualification() != null && !director.getQualification().isEmpty();
    }

    public boolean isLessonsExist() {
        return director.getClasses() != null && !director.getClasses().isEmpty();
    }

    public boolean isDisciplinesExist() {
        return director.getDisciplines() != null && !director.getDisciplines().isEmpty();
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public boolean isEditPersonalInformation() {
        return editPersonalInformation;
    }

    public void setEditPersonalInformation(boolean editPersonalInformation) {
        this.editPersonalInformation = editPersonalInformation;
    }

    public boolean isEditTeacherOpportunities() {
        return editTeacherOpportunities;
    }

    public void setEditTeacherOpportunities(boolean editTeacherOpportunities) {
        this.editTeacherOpportunities = editTeacherOpportunities;
    }

    public boolean isAddQualification() {
        return addQualification;
    }

    public void setAddQualification(boolean addQualification) {
        this.addQualification = addQualification;
    }

    public boolean isAddDiscipline() {
        return addDiscipline;
    }

    public void setAddDiscipline(boolean addDiscipline) {
        this.addDiscipline = addDiscipline;
    }

    public boolean isAddLesson() {
        return addLesson;
    }

    public void setAddLesson(boolean addLesson) {
        this.addLesson = addLesson;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
