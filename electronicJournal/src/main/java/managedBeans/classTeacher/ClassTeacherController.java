package managedBeans.classTeacher;

import beans.ClassBean;
import beans.ClassTeacherBean;
import beans.PersonBean;
import beans.StudentBean;
import entities.*;
import org.apache.log4j.Logger;
import utils.Helper;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
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
import java.util.Locale;

/**
 * Created by win10 on 06.12.2016.
 */

@ManagedBean(name = "classTeacherController", eager = true)
@SessionScoped
public class ClassTeacherController {

    private final static Logger logger = Logger.getLogger(ClassTeacherController.class);

    @EJB
    private ClassTeacherBean classTeacherBean;
    @EJB
    private StudentBean studentBean;
    @EJB
    private ClassBean classBean;
    @EJB
    private PersonBean personBean;

    private ClassTeacher classTeacher;
    private School school;
    private Director director;
    private ClassName className;

    private String birthStr;
    private boolean addStudent;
    private boolean editStudent;
    private boolean dissolveClass;
    private String name;
    private String patronymic;
    private String surname;
    private Date birth;
    private String phone;
    private String mail;
    private String address;
    private String additionalInformation;
    private Student student;


    private void init() {
        addStudent = false;
        editStudent = false;
    }

    public String toClassPage() {
        init();
        return "/classTeacher/class?faces-redirect=true";
    }

    public String toHomePage() {
        init();
        return "/classTeacher/classTeacherIndex?faces-redirect=true";
    }

    public String cancelAddStudent() {
        addStudent = false;
        return "/classTeacher/class?faces-redirect=true";
    }

    public String addStudent() throws NoSuchAlgorithmException {

        Student student = new Student(name, patronymic, surname, birth, phone, address, mail, "password", mail,
                additionalInformation, school, className);

        Student addedStudent = studentBean.add(student);
        getClassName().getStudents().add(addedStudent);
        classBean.update(getClassName());

        addStudent = false;
        return "/classTeacher/class?faces-redirect=true";
    }

    public void validateForm(ComponentSystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();

        String patronymic = Helper.getInformation("patronymic", components);
        String name = Helper.getInformation("name", components);
        String surname = Helper.getInformation("surname", components);
        String phone = Helper.getInformation("phone", components);
        String mail = Helper.getInformation("mail", components);
        String address = Helper.getInformation("address", components);

        FacesMessage message = null;
        if (name.isEmpty() || patronymic.isEmpty() || surname.isEmpty() || phone.isEmpty()
                || mail.isEmpty() || address.isEmpty()) {
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
                } else if (!checkMail) {
                    message = new FacesMessage("Wrong e-mail format!");
                    logger.warn("Wrong e-mail(" + mail + ") format!");
                    ((UIInput) components.findComponent("mail")).setValue("");
                } else {
                    message = new FacesMessage("Wrong phone number format!");
                    logger.warn("Wrong phone number(" + phone + ") format!");
                    ((UIInput) components.findComponent("phone")).setValue("");
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

    public ClassTeacher getClassTeacher() {
        classTeacher = classTeacher == null ? classTeacherBean.getByLogin(Helper.getCurrentUser()) : classTeacher;
        return classTeacher;
    }

    public String getClassTeacherBirth() {
        Calendar birth = Calendar.getInstance(Locale.ROOT);
        birth.setTime(classTeacher.getBirth());
        return birth.get(Calendar.DATE) + "-" + (birth.get(Calendar.MONTH) + 1) + "-" + birth.get(Calendar.YEAR);
    }

    public boolean isDisciplinesExist() {
        return classTeacher.getDisciplines() != null && !classTeacher.getDisciplines().isEmpty();
    }

    public boolean isLessonsExist() {
        return classTeacher.getClasses() != null && !classTeacher.getClasses().isEmpty();
    }

    public School getSchool() {
        return school == null ? classTeacher.getSchool() : school;
    }

    public Director getDirector() {
        return director == null ? getSchool().getDirector() : director;
    }

    public ClassName getClassName() {
        return className == null ? classTeacher.getClassName() : className;
    }


    public String getDirectorBirth() {
        Calendar date = Calendar.getInstance(Locale.ROOT);
        date.setTime(getDirector().getBirth());
        return date.get(Calendar.DATE) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.YEAR);
    }

    public String toSchoolPage() {
        init();
        return "/classTeacher/school?faces-redirect=true";
    }

    public String toAddStudent() {
        init();
        addStudent = true;

        name = "";
        patronymic = "";
        surname = "";
        birth = null;
        phone = "";
        mail = "";
        address = "";
        additionalInformation = "";

        return "/classTeacher/addStudent";
    }

    public String studentDetails(Student student) {
        init();
        setStudent(student);
        return "/classTeacher/studentDetails";
    }

    public String cancelSaveStudent() {
        editStudent = false;
        return "/classTeacher/studentDetails";
    }

    public String saveStudent() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        student.setBirth(simpleDateFormat.parse(birthStr));
        studentBean.update(student);

        editStudent = false;
        return "/classTeacher/studentDetails";
    }

    public String toEditStudent() {
        init();
        editStudent = true;
        return "/classTeacher/studentDetails";
    }

    public boolean isDissolveClass() {
        return getClassName().getStudents().isEmpty();
    }

    public String getBirthStr() {
        Calendar birth = Calendar.getInstance(Locale.ROOT);
        birth.setTime(student.getBirth());
        return birth.get(Calendar.DATE) + "-" + (birth.get(Calendar.MONTH) + 1) + "-" + birth.get(Calendar.YEAR);
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDissolveClass(boolean dissolveClass) {
        this.dissolveClass = dissolveClass;
    }

    public boolean isEditStudent() {
        return editStudent;
    }

    public void setEditStudent(boolean editStudent) {
        this.editStudent = editStudent;
    }

    public boolean isAddStudent() {
        return addStudent;
    }

    public void setAddStudent(boolean addStudent) {
        this.addStudent = addStudent;
    }

    public void setBirthStr(String birthStr) {
        this.birthStr = birthStr;
    }

    public void setClassName(ClassName className) {
        this.className = className;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public void setClassTeacher(ClassTeacher classTeacher) {
        this.classTeacher = classTeacher;
    }
}
