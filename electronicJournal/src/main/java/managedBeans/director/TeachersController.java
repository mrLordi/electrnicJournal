package managedBeans.director;

import beans.*;
import entities.*;
import org.apache.log4j.Logger;
import utils.Helper;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by win10 on 28.11.2016.
 */

@ManagedBean(name = "teachersBean", eager = true)
@SessionScoped
public class TeachersController {

    private final static Logger logger = Logger.getLogger(TeachersController.class);

    @EJB
    private TeacherBean teacherBean;

    @EJB
    private DisciplineForClassBean disciplineForClassBean;

    @EJB
    private DisciplineBean disciplineBean;

    @EJB
    private PersonBean personBean;

    @EJB
    private ClassTeacherBean classTeacherBean;

    @EJB
    private ClassBean classBean;

    @ManagedProperty(value = "#{directorBean}")
    private DirectorController directorController;

    private String name;
    private String patronymic;
    private String surname;
    private Date birth;
    private String phone;
    private String address;
    private String mail;
    private String additionalInformation;
    private String qualification;
    private String className;

    private String discipline;

    private String teacherBirthStr;
    private String classTeacherBirthStr;

    private Teacher teacher;
    private ClassTeacher classTeacher;

    private boolean editTeacher;
    private boolean editClassTeacher;

    private boolean addTeacher;
    private boolean addClassTeacher;

    private boolean addDisciplineForTeacher;
    private boolean addDisciplineForClassTeacher;

    private boolean addLessonForTeacher;
    private boolean addLessonForClassTeacher;

    private School school;

    private void init() {
        addTeacher = false;
        addClassTeacher = false;

        editTeacher = false;
        editClassTeacher = false;

        addDisciplineForTeacher = false;
        addDisciplineForClassTeacher = false;

        addLessonForTeacher = false;
        addLessonForClassTeacher = false;
    }

    public String toTeachersPage() {
        init();

        return "/director/teachers/teachers?faces-redirect=true";
    }

    public String toClassesPage() {
        init();

        return "/director/classes/classes?faces-redirect=true";
    }

    public String toSchoolPage() {
        init();

        return "/director/school?faces-redirect=true";
    }

    public String toHomePage() {
        init();

        return "/director/directorIndex?faces-redirect=true";
    }


    public String toAddTeacher() {
        init();
        addTeacher = true;

        reset();

        return "/director/teachers/addTeachers";
    }

    public String toAddClassTeacher() {
        init();
        addClassTeacher = true;

        reset();
        className = "";

        return "/director/teachers/addTeachers";
    }

    private void reset() {
        name = "";
        patronymic = "";
        surname = "";
        birth = null;
        phone = "";
        mail = "";
        address = "";
        additionalInformation = "";
        qualification = "";
    }

    public String toEditTeacher() {
        init();
        editTeacher = true;
        return "/director/teachers/teacherDetails";
    }

    public String toEditClassTeacher() {
        init();
        editClassTeacher = true;
        return "/director/teachers/classTeacherDetails";
    }

    public boolean isTeacherExist() {
        List<Teacher> teachers = teacherBean.getAllBySchoolId(getSchool());
        return teachers != null && !teachers.isEmpty();
    }

    public boolean isClassTeacherExist() {
        List<ClassTeacher> teachers = classTeacherBean.getAllBySchoolId(getSchool());
        return teachers != null && !teachers.isEmpty();
    }

    public void validateForm(ComponentSystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();

        String name = Helper.getInformation("teacherName", components);
        String patronymic = Helper.getInformation("teacherPatronymic", components);
        String surname = Helper.getInformation("teacherSurname", components);
        String phone = Helper.getInformation("teacherPhone", components);
        String mail = Helper.getInformation("teacherMail", components);
        String address = Helper.getInformation("teacherAddress", components);
        String qualification = Helper.getInformation("teacherQualification", components);

        FacesMessage message = null;
        if (name.isEmpty() || patronymic.isEmpty() || surname.isEmpty() || phone.isEmpty()
                || mail.isEmpty() || address.isEmpty() || qualification.isEmpty()) {
            message = new FacesMessage("Fill in all required fields!");
            logger.warn("All required fields must be filled!");
        } else {
            boolean checkPhone = Helper.checkPhone(phone);
            boolean checkMail = Helper.checkMail(mail);

            if (!checkPhone || !checkMail) {
                if (!checkPhone && !checkMail) {
                    message = new FacesMessage("Wrong phone number and e-mail format!");
                    logger.warn("Wrong phone number(" + phone + ") and e-mail(" + mail + ") format!");
                    ((UIInput) components.findComponent("teacherPhone")).setValue("");
                    ((UIInput) components.findComponent("teacherMail")).setValue("");
                } else if (!checkPhone) {
                    message = new FacesMessage("Wrong phone number format!");
                    logger.warn("Wrong phone number(" + phone + ") format!");
                    ((UIInput) components.findComponent("teacherPhone")).setValue("");
                } else {
                    message = new FacesMessage("Wrong e-mail format!");
                    logger.warn("Wrong e-mail(" + mail + ") format!");
                    ((UIInput) components.findComponent("teacherMail")).setValue("");
                }
            }
        }

        Person person = personBean.getByMail(mail);
        if (person != null) {
            message = new FacesMessage("This email address is already taken!");
            logger.warn("Email address (" + mail + ") is already taken!");
            ((UIInput) components.findComponent("teacherMail")).setValue("");
        }

        if (addClassTeacher) {
            String className = Helper.getInformation("className", components);
            ClassName clazz = classBean.getByName(className);
            if (clazz == null) {
                message = new FacesMessage("Class with name '" + className + "' does not exist!");
                logger.warn("Class with name '" + className + "' does not exist!");
            }
        }

        if (message != null) {
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(components.getClientId(), message);
            facesContext.renderResponse();
        }

    }

    public String cancelAddTeacher() {
        init();
        return "/director/teachers/teachers";
    }

    public String addTeacher() throws NoSuchAlgorithmException {

        Teacher teacher = new Teacher();

        teacher.setName(name);
        teacher.setPatronymic(patronymic);
        teacher.setSurname(surname);
        teacher.setBirth(birth);
        teacher.setPhoneNumber(phone);
        teacher.setAddress(address);
        teacher.setMail(mail);
        teacher.setQualification(qualification);
        teacher.setSchool(getSchool());

        teacher.setLogin(mail);
        teacher.setPassword("password");
        teacher.setAdditional(additionalInformation);

        teacherBean.add(teacher);

        addTeacher = false;
        return "/director/teachers/teachers";
    }

    public String addClassTeacher() throws NoSuchAlgorithmException {

        ClassTeacher teacher = new ClassTeacher();

        teacher.setName(name);
        teacher.setPatronymic(patronymic);
        teacher.setSurname(surname);
        teacher.setBirth(birth);
        teacher.setPhoneNumber(phone);
        teacher.setAddress(address);
        teacher.setMail(mail);
        teacher.setQualification(qualification);
        teacher.setSchool(getSchool());

        teacher.setLogin(mail);
        teacher.setPassword("password");
        teacher.setClassName(classBean.getByName(className));
        teacher.setAdditional(additionalInformation);

        classTeacherBean.add(teacher);

        addClassTeacher = false;
        return "/director/teachers/teachers";
    }

    public String teacherDetails(Teacher teacher) {
        init();
        setTeacher(teacher);
        return "/director/teachers/teacherDetails";
    }

    public String classTeacherDetails(ClassTeacher teacher) {
        init();
        setClassTeacher(teacher);
        return "/director/teachers/classTeacherDetails";
    }

    public List<Teacher> getAllTeachers() {
        return teacherBean.getAllBySchoolId(getSchool());
    }

    public List<ClassTeacher> getAllClassTeachers() {
        return classTeacherBean.getAllBySchoolId(getSchool());
    }

    public String getTeacherBirthStr() {
        Calendar birth = Calendar.getInstance(Locale.ROOT);
        birth.setTime(teacher.getBirth());
        return birth.get(Calendar.DATE) + "-" + (birth.get(Calendar.MONTH) + 1) + "-" + birth.get(Calendar.YEAR);
    }

    public String getClassTeacherBirthStr() {
        Calendar birth = Calendar.getInstance(Locale.ROOT);
        birth.setTime(classTeacher.getBirth());
        return birth.get(Calendar.DATE) + "-" + (birth.get(Calendar.MONTH) + 1) + "-" + birth.get(Calendar.YEAR);
    }

    public void setClassTeacherBirthStr(String birthStr) {
        this.classTeacherBirthStr = birthStr;
    }

    public String cancelSaveTeacher() {
        editTeacher = false;
        return "/director/teachers/teacherDetails";
    }

    public String cancelSaveClassTeacher() {
        editClassTeacher = false;
        return "/director/teachers/classTeacherDetails";
    }

    public String saveTeacher() throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        teacher.setBirth(simpleDateFormat.parse(teacherBirthStr));
        teacherBean.update(teacher);

        editTeacher = false;
        return "/director/teachers/teacherDetails";
    }

    public String saveClassTeacher() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        classTeacher.setBirth(simpleDateFormat.parse(classTeacherBirthStr));
        classTeacherBean.update(classTeacher);

        editClassTeacher = false;
        return "/director/teachers/classTeacherDetails";
    }

    public School getSchool() {
        school = school == null ? directorController.getDirector().getSchool() : school;
        return school;
    }

    public String dismissalTeacher() {
        teacher.setSchool(null);
        teacherBean.update(teacher);
        return "/director/teachers/teachers";
    }

    public String dismissalClassTeacher() {
        classTeacher.setSchool(null);
        classTeacher.setClassName(null);

        classTeacherBean.update(classTeacher);
        return "/director/teachers/teachers";
    }

    public boolean isDisciplinesInTeacherExist() {
        return teacher.getDisciplines() != null && !teacher.getDisciplines().isEmpty();
    }

    public boolean isDisciplinesInClassTeacherExist() {
        return classTeacher.getDisciplines() != null && !classTeacher.getDisciplines().isEmpty();
    }

    public String deleteDisciplineForTeacher(Discipline discipline) {
        teacher.getDisciplines().remove(discipline);
        teacherBean.update(teacher);
        return "director/teachers/teacherDetails";
    }

    public String deleteDisciplineForClassTeacher(Discipline discipline) {
        classTeacher.getDisciplines().remove(discipline);
        classTeacherBean.update(classTeacher);
        return "director/teachers/classTeacherDetails";
    }

    public void validateDiscipline(ComponentSystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();

        String discipline = Helper.getInformation("discipline", components);

        if (discipline.isEmpty()) {
            FacesMessage message = new FacesMessage("Fill in required 'theme' field!");
            logger.warn("Required field 'theme' must be filled!");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(components.getClientId(), message);
            facesContext.renderResponse();
        }
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String toAddDisciplineForTeacher() {
        init();
        addDisciplineForTeacher = true;
        discipline = "";

        return "director/teachers/teacherDetails";
    }

    public String toAddDisciplineForClassTeacher() {
        init();
        addDisciplineForClassTeacher = true;
        discipline = "";

        return "director/teachers/classTeacherDetails";
    }

    public String addDisciplineForTeacher() {
        Discipline disciplineForAdd = disciplineBean.getByName(discipline);

        if (disciplineForAdd == null) {
            disciplineForAdd = new Discipline(discipline);
            disciplineForAdd = disciplineBean.add(disciplineForAdd);
        }
        teacher.getDisciplines().add(disciplineForAdd);
        teacherBean.update(teacher);

        addDisciplineForTeacher = false;
        return "director/teachers/teacherDetails";
    }

    public String addDisciplineForClassTeacher() {
        Discipline disciplineForAdd = disciplineBean.getByName(discipline);

        if (disciplineForAdd == null) {
            disciplineForAdd = new Discipline(discipline);
            disciplineForAdd = disciplineBean.add(disciplineForAdd);
        }
        classTeacher.getDisciplines().add(disciplineForAdd);
        classTeacherBean.update(classTeacher);

        addDisciplineForClassTeacher = false;
        return "director/teachers/classTeacherDetails";
    }

    public String cancelAddDisciplineForTeacher() {
        addDisciplineForTeacher = false;
        return "director/teachers/teacherDetails";
    }

    public String cancelAddDisciplineForClassTeacher() {
        addDisciplineForClassTeacher = false;
        return "director/teachers/classTeacherDetails";
    }

    public boolean isLessonInTeacherExist() {
        return teacher.getClasses() != null && !teacher.getClasses().isEmpty();
    }

    public boolean isLessonInClassTeacherExist() {
        return classTeacher.getClasses() != null && !classTeacher.getClasses().isEmpty();
    }

    public String toAddLessonForTeacher() {
        init();
        addLessonForTeacher = true;
        className = "";
        discipline = "";
        return "director/teachers/teacherDetails";
    }

    public String toAddLessonForClassTeacher() {
        init();
        addLessonForClassTeacher = true;
        className = "";
        discipline = "";
        return "director/teachers/classTeacherDetails";
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

        } else {

            ClassName clazz = classBean.getByName(className);
            if (clazz == null) {
                message = new FacesMessage("Class with name '" + className + "' does not exist!");
                logger.warn("Class with name '" + className + "' does not exist!");
            }
        }

        if (message != null) {
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(components.getClientId(), message);
            facesContext.renderResponse();
        }

    }

    public String addLessonForTeacher() {
        Discipline disciplineForAdd = disciplineBean.getByName(discipline);

        if (disciplineForAdd == null) {

            disciplineForAdd = new Discipline(discipline);

            teacher.getDisciplines().add(disciplineBean.add(disciplineForAdd));
            teacherBean.update(teacher);
        }

        DisciplineForClass disciplineForClass = new DisciplineForClass();
        disciplineForClass.setClassName(classBean.getByName(className));
        disciplineForClass.setTeacher(teacher);

        disciplineForClass.setDiscipline(disciplineBean.getByName(discipline));

        DisciplineForClass forClass = disciplineForClassBean.add(disciplineForClass);

        teacher.getClasses().add(forClass);

        addLessonForTeacher = false;
        return "director/teachers/teacherDetails";
    }

    public String addLessonForClassTeacher() {
        Discipline disciplineForAdd = disciplineBean.getByName(discipline);

        if (disciplineForAdd == null) {

            disciplineForAdd = new Discipline(discipline);

            classTeacher.getDisciplines().add(disciplineBean.add(disciplineForAdd));
            classTeacherBean.update(classTeacher);
        }

        DisciplineForClass disciplineForClass = new DisciplineForClass();
        disciplineForClass.setClassName(classBean.getByName(className));
        disciplineForClass.setTeacher(classTeacher);

        disciplineForClass.setDiscipline(disciplineBean.getByName(discipline));

        DisciplineForClass forClass = disciplineForClassBean.add(disciplineForClass);

        classTeacher.getClasses().add(forClass);

        addLessonForClassTeacher = false;
        return "director/teachers/classTeacherDetails";
    }

    public String cancelAddLessonForTeacher() {
        addLessonForTeacher = false;
        return "director/teachers/teacherDetails";
    }

    public String cancelAddLessonForClassTeacher() {
        addLessonForClassTeacher = false;
        return "director/teachers/classTeacherDetails";
    }

    public DirectorController getDirectorController() {
        return directorController;
    }

    public void setDirectorController(DirectorController directorController) {
        this.directorController = directorController;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setTeacherBirthStr(String teacherBirthStr) {
        this.teacherBirthStr = teacherBirthStr;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public ClassTeacher getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(ClassTeacher classTeacher) {
        this.classTeacher = classTeacher;
    }

    public boolean isEditTeacher() {
        return editTeacher;
    }

    public void setEditTeacher(boolean editTeacher) {
        this.editTeacher = editTeacher;
    }

    public boolean isEditClassTeacher() {
        return editClassTeacher;
    }

    public void setEditClassTeacher(boolean editClassTeacher) {
        this.editClassTeacher = editClassTeacher;
    }

    public boolean isAddTeacher() {
        return addTeacher;
    }

    public void setAddTeacher(boolean addTeacher) {
        this.addTeacher = addTeacher;
    }

    public boolean isAddClassTeacher() {
        return addClassTeacher;
    }

    public void setAddClassTeacher(boolean addClassTeacher) {
        this.addClassTeacher = addClassTeacher;
    }

    public boolean isAddDisciplineForTeacher() {
        return addDisciplineForTeacher;
    }

    public void setAddDisciplineForTeacher(boolean addDisciplineForTeacher) {
        this.addDisciplineForTeacher = addDisciplineForTeacher;
    }

    public boolean isAddDisciplineForClassTeacher() {
        return addDisciplineForClassTeacher;
    }

    public void setAddDisciplineForClassTeacher(boolean addDisciplineForClassTeacher) {
        this.addDisciplineForClassTeacher = addDisciplineForClassTeacher;
    }

    public boolean isAddLessonForTeacher() {
        return addLessonForTeacher;
    }

    public void setAddLessonForTeacher(boolean addLessonForTeacher) {
        this.addLessonForTeacher = addLessonForTeacher;
    }

    public boolean isAddLessonForClassTeacher() {
        return addLessonForClassTeacher;
    }

    public void setAddLessonForClassTeacher(boolean addLessonForClassTeacher) {
        this.addLessonForClassTeacher = addLessonForClassTeacher;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}

