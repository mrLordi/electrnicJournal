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
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by win10 on 29.11.2016.
 */

@ManagedBean(name = "classBean", eager = true)
@SessionScoped
public class ClassesController {

    private final static Logger logger = Logger.getLogger(ClassesController.class);

    @EJB
    private ClassBean classBean;

    @EJB
    private PersonBean personBean;

    @EJB
    private SchoolBean schoolBean;

    @EJB
    private TeacherBean teacherBean;

    @EJB
    private ClassTeacherBean classTeacherBean;

    @ManagedProperty(value = "#{directorBean}")
    private DirectorController directorController;

    private boolean addNewClassTeacher;
    private boolean addClassTeacher;
    private boolean addExistingTeacher;
    private boolean addClass;
    private boolean editClass;
    private boolean dissolveClass;

    private String teacherLogin;
    private String name;
    private String patronymic;
    private String surname;
    private Date birth;
    private String phone;
    private String address;
    private String mail;
    private String additionalInformation;
    private String qualification;

    private ClassName className;
    private School school;

    private void init() {
        addNewClassTeacher = false;
        addClassTeacher = false;
        addExistingTeacher = false;
        addClass = false;
        editClass = false;
    }

    public String toSendMessage() {
        init();
        return "/director/sendMessage?faces-redirect=true";
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

    public String toAddExistingTeacher() {
        init();
        addClassTeacher = true;
        addExistingTeacher = true;
        teacherLogin = "";
        return "/director/classes/classDetails?faces-redirect=true";
    }

    public String addNewClassTeacher() throws NoSuchAlgorithmException {

        ClassTeacher classTeacher = new ClassTeacher(name, patronymic, surname, birth, phone, address, mail,
                "password", mail, additionalInformation, getSchool(), qualification, className);

        className.setClassTeacher(classTeacherBean.add(classTeacher));
        classBean.update(className);

        addNewClassTeacher = false;
        addClassTeacher = false;
        return "/director/classes/classDetails?faces-redirect=true";
    }

    public String cancelAddNewClassTeacher() {

        addNewClassTeacher = false;
        addClassTeacher = false;
        return "/director/classes/classDetails?faces-redirect=true";
    }

    public String cancelAddExistingTeacher() {
        addExistingTeacher = false;
        addClassTeacher = false;
        return "/director/classes/classDetails?faces-redirect=true";
    }

    public String addExistingTeacher() throws NoSuchAlgorithmException {
        Teacher teacher = teacherBean.getByLogin(teacherLogin);

        ClassTeacher classTeacher = new ClassTeacher(teacher.getName(), teacher.getPatronymic(), teacher.getSurname(),
                teacher.getBirth(), teacher.getPhoneNumber(), teacher.getAddress(), teacher.getLogin(), "password",
                teacher.getMail(), teacher.getAdditional(), teacher.getSchool(), teacher.getQualification(), className );

        classTeacher.setDisciplines(teacher.getDisciplines());
        classTeacher.setClasses(teacher.getClasses());

        teacherBean.delete(teacher);
        className.setClassTeacher(classTeacher);
        classTeacherBean.add(classTeacher);

        addExistingTeacher = false;
        addClassTeacher = false;
        return "/director/classes/classDetails?faces-redirect=true";
    }

    public String toAddClassTeacher() {
        init();
        addClassTeacher = true;
        return "/director/classes/classDetails?faces-redirect=true";
    }

    public boolean isClassTeacherExist() {
        return getClassName().getClassTeacher() != null;
    }

    public boolean isDissolveClass() {
        return getClassName().getStudents().isEmpty();
    }


    public Set<Student> getAllStudent() {
        return getClassName().getStudents();
    }

    public String toEditClass() {
        init();
        editClass = true;
        return "/director/classes/classDetails";
    }

    public String toAddNewClassTeacher() {
        init();
        addNewClassTeacher = true;

        name = "";
        patronymic = "";
        surname = "";
        birth = null;
        phone = "";
        address = "";
        mail = "";
        additionalInformation = "";
        qualification = "";

        return "/director/classes/addNewClassTeacher?faces-redirect=true";
    }

    public String cancelSaveClass() {
        editClass = false;
        return "/director/classes/classDetails";
    }

    public String saveClass() {
        classBean.update(className);

        editClass = false;
        return "/director/classes/classDetails";
    }

    public String dissolveClass() throws NoSuchAlgorithmException {

        ClassTeacher classTeacher = className.getClassTeacher();
        if (classTeacher != null) {

            Teacher teacher = new Teacher(classTeacher.getName(), classTeacher.getPatronymic(),
                    classTeacher.getSurname(), classTeacher.getBirth(), classTeacher.getPhoneNumber(),
                    classTeacher.getAddress(), classTeacher.getLogin(), "password", classTeacher.getMail(),
                    classTeacher.getAdditional(), classTeacher.getSchool(), classTeacher.getQualification());

            teacher.setDisciplines(classTeacher.getDisciplines());
            teacher.setClasses(classTeacher.getClasses());

            classTeacherBean.delete(classTeacher);
            teacherBean.add(teacher);
        }

        classBean.delete(className);

        return "/director/classes/classes";
    }

    public String cancelAddClass() {
        addClass = false;
        return "/director/classes/classes";
    }

    public String addClass() {

        ClassName className = new ClassName(name, getSchool());

        classBean.add(className);

        addClass = false;
        return "/director/classes/classes";
    }

    public void validateForm(ComponentSystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();

        String name = Helper.getInformation("name", components);

        if (name.isEmpty()) {
            FacesMessage message = new FacesMessage("Fill in 'name' fields!");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            logger.warn("Required field 'name' must be filled!");
            facesContext.addMessage(components.getClientId(), message);
            facesContext.renderResponse();
        }
    }

    public void validateTeacherLogin(ComponentSystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();

        String teacherLogin = Helper.getInformation("teacherLogin", components);

        FacesMessage message = null;
        if (teacherLogin.isEmpty()) {
            message = new FacesMessage("Fill in 'login' fields!");
            logger.warn("Required field 'login' must be filled!");
        } else if (teacherBean.getByLogin(teacherLogin) == null) {
            message = new FacesMessage("Teacher with login '" + teacherLogin + "' does not exist!");
            logger.warn("Teacher with login '" + teacherLogin + "' does not exist!");
        }

        if (message != null) {
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(components.getClientId(), message);
            facesContext.renderResponse();
        }

    }

    public void validateClassTeacher(ComponentSystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();

        String name = Helper.getInformation("name", components);
        String patronymic = Helper.getInformation("patronymic", components);
        String surname = Helper.getInformation("surname", components);
        String phone = Helper.getInformation("phone", components);
        String mail = Helper.getInformation("mail", components);
        String address = Helper.getInformation("address", components);
        String qualification = Helper.getInformation("qualification", components);

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
                    ((UIInput) components.findComponent("phone")).setValue("");
                    ((UIInput) components.findComponent("mail")).setValue("");
                } else if (!checkPhone) {
                    message = new FacesMessage("Wrong phone number format!");
                    logger.warn("Wrong phone number(" + phone + ") format!");
                    ((UIInput) components.findComponent("phone")).setValue("");
                } else {
                    message = new FacesMessage("Wrong e-mail format!");
                    logger.warn("Wrong e-mail(" + mail + ") format!");
                    ((UIInput) components.findComponent("mail")).setValue("");
                }
            }
        }

        Person person = personBean.getByMail(mail);
        if (person != null) {
            message = new FacesMessage("This email address is already taken!");
            logger.warn("Email address (" + mail + ") is already taken!");
            ((UIInput) components.findComponent("mail")).setValue("");
        }

        if (message != null) {
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(components.getClientId(), message);
            facesContext.renderResponse();
        }

    }

    public String toAddClass() {
        init();
        addClass = true;

        name = "";

        return "/director/classes/classes";
    }

    public String classDetails(ClassName className) {
        this.className = className;
        return "/director/classes/classDetails?faces-redirect=true";
    }

    public List<ClassName> getAllClasses() {
        return classBean.getAllBySchoolId(getSchool());
    }

    public boolean isClassesExist() {
        List<ClassName> classNameList = classBean.getAllBySchoolId(getSchool());
        return classNameList != null && !classNameList.isEmpty();
    }

    public School getSchool() {
        school = school == null ? directorController.getDirector().getSchool() : school;
        return school;
    }


    public DirectorController getDirectorController() {
        return directorController;
    }

    public void setDirectorController(DirectorController directorController) {
        this.directorController = directorController;
    }

    public boolean isAddNewClassTeacher() {
        return addNewClassTeacher;
    }

    public void setAddNewClassTeacher(boolean addNewClassTeacher) {
        this.addNewClassTeacher = addNewClassTeacher;
    }

    public boolean isAddClassTeacher() {
        return addClassTeacher;
    }

    public void setAddClassTeacher(boolean addClassTeacher) {
        this.addClassTeacher = addClassTeacher;
    }

    public boolean isAddExistingTeacher() {
        return addExistingTeacher;
    }

    public void setAddExistingTeacher(boolean addExistingTeacher) {
        this.addExistingTeacher = addExistingTeacher;
    }

    public boolean isAddClass() {
        return addClass;
    }

    public void setAddClass(boolean addClass) {
        this.addClass = addClass;
    }

    public boolean isEditClass() {
        return editClass;
    }

    public void setEditClass(boolean editClass) {
        this.editClass = editClass;
    }

    public void setDissolveClass(boolean dissolveClass) {
        this.dissolveClass = dissolveClass;
    }

    public String getTeacherLogin() {
        return teacherLogin;
    }

    public void setTeacherLogin(String teacherLogin) {
        this.teacherLogin = teacherLogin;
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

    public ClassName getClassName() {
        return className;
    }

    public void setClassName(ClassName className) {
        this.className = className;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
