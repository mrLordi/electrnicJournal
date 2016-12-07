package managedBeans.director;

import beans.ClassBean;
import beans.PersonBean;
import beans.StudentBean;
import entities.ClassName;
import entities.Person;
import entities.School;
import entities.Student;
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
import java.util.Locale;

/**
 * Created by win10 on 29.11.2016.
 */

@ManagedBean(name = "studentBean", eager = true)
@SessionScoped
public class StudentController {

    private final static Logger logger = Logger.getLogger(StudentController.class);

    @EJB
    private StudentBean studentBean;
    @EJB
    private ClassBean classBean;
    @EJB
    private PersonBean personBean;

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
    private ClassName className;

    private String birthStr;

    private boolean addStudent;
    private boolean editStudent;
    private Student student;
    private School school;

    private void init() {
        addStudent = false;
        editStudent = false;
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

    public String toTransferStudent() {
        init();
        // TODO: transfer student
        return "/director/classes/studentDetails";
    }

    public String excludeStudent() {
        ClassName tmp = student.getClassName();

        student.setClassName(null);
        student.setSchool(null);

        tmp.getStudents().remove(student);

        studentBean.update(student);

        return "/director/classes/classDetails?faces-redirect=true";
    }

    public String toEditStudent() {
        init();
        editStudent = true;
        return "/director/classes/studentDetails";
    }

    public String cancelSaveStudent() {
        editStudent = false;
        return "/director/classes/studentDetails";
    }

    public String saveStudent() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        student.setBirth(simpleDateFormat.parse(birthStr));
        studentBean.update(student);

        editStudent = false;
        return "/director/classes/studentDetails";
    }

    public String getBirthStr() {
        Calendar birth = Calendar.getInstance(Locale.ROOT);
        birth.setTime(getStudent().getBirth());
        return birth.get(Calendar.DATE) + "-" + (birth.get(Calendar.MONTH) + 1) + "-" + birth.get(Calendar.YEAR);
    }

    public String studentDetails(Student student) {
        init();
        setStudent(student);
        return "/director/classes/studentDetails";
    }

    public String toAddStudent(ClassName className) {
        init();
        addStudent = true;
        setClassName(className);

        name = "";
        patronymic = "";
        surname = "";
        birth = null;
        phone = "";
        mail = "";
        address = "";
        additionalInformation = "";

        return "/director/classes/addStudent";
    }

    public String cancelAddStudent() {
        addStudent = false;
        return "/director/classes/classDetails";
    }

    public String addStudent() throws NoSuchAlgorithmException {
        Student student = new Student(name, patronymic, surname, birth, phone, address, mail, "password", mail,
                additionalInformation, school, className);

        className.getStudents().add(studentBean.add(student));
        classBean.update(className);

        addStudent = false;
        return "/director/classes/classDetails";
    }

    public School getSchool() {
        school = school == null ? directorController.getDirector().getSchool() : school;
        return school;
    }

    public void validateForm(ComponentSystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();


        String name = Helper.getInformation("name", components);
        String patronymic = Helper.getInformation("patronymic", components);
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
                } else if (!checkPhone) {
                    message = new FacesMessage("Wrong phone number!");
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

    public ClassName getClassName() {
        return className;
    }

    public void setClassName(ClassName className) {
        this.className = className;
    }

    public void setBirthStr(String birthStr) {
        this.birthStr = birthStr;
    }

    public boolean isAddStudent() {
        return addStudent;
    }

    public void setAddStudent(boolean addStudent) {
        this.addStudent = addStudent;
    }

    public boolean isEditStudent() {
        return editStudent;
    }

    public void setEditStudent(boolean editStudent) {
        this.editStudent = editStudent;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
