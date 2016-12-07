package managedBeans;

import beans.DirectorBean;
import beans.PersonBean;
import entities.Director;
import entities.Person;
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
import java.util.Date;

/**
 * Created by win10 on 23.11.2016.
 */
@ManagedBean(name = "accountBean", eager = true)
@SessionScoped
public class AccountController {
    private final static Logger logger = Logger.getLogger(AccountController.class);

    @EJB
    private DirectorBean directorBean;
    @EJB
    private PersonBean personBean;

    private String login;
    private String password;

    private String name;
    private String patronymic;
    private String surname;
    private Date birth;
    private String phone;
    private String address;
    private String mail;
    private String additionalInformation;


    public String signUp() throws NoSuchAlgorithmException {
        Director director = new Director();
        director.setName(name);
        director.setPatronymic(patronymic);
        director.setSurname(surname);
        director.setBirth(birth);
        director.setPhoneNumber(phone);
        director.setAddress(address);
        director.setMail(mail);

        director.setLogin(login);
        director.setPassword(password);
        if (!additionalInformation.isEmpty()) {
            director.setAdditional(additionalInformation);
        }

        directorBean.add(director);
        return "/index?faces-redirect=true";
    }

    public String toRegistration() {
        login = "";
        password = "";
        name = "";
        patronymic = "";
        surname = "";
        birth = null;
        phone = "";
        mail = "";
        address = "";
        additionalInformation = "";
        return "registration";
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

        String login = Helper.getInformation("login", components);
        String password = Helper.getInformation("password", components);
        String confirmPassword = Helper.getInformation("confirmPassword", components);


        FacesMessage message = null;
        if (name.isEmpty() || patronymic.isEmpty() || surname.isEmpty() || phone.isEmpty() || mail.isEmpty()
                || address.isEmpty() || login.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            message = new FacesMessage("Fill in all required fields!");
            logger.warn("All required fields must be filled!");
        } else {
            boolean checkPhone = Helper.checkPhone(phone);
            boolean checkMail = Helper.checkMail(mail);

            if (!checkPhone || !checkMail) {
                if (!checkPhone && !checkMail) {
                    message = new FacesMessage("Wrong phone number and e-mail format!");
                    logger.warn("Wrong phone number(" + phone + ") and e-mail(" + mail + ") format!");
                    ((UIInput) components.findComponent("mail")).setValue("");
                    ((UIInput) components.findComponent("phone")).setValue("");
                } else if (!checkPhone) {
                    message = new FacesMessage("Wrong phone format!");
                    logger.warn("Wrong phone number(" + phone + ") format!");
                    ((UIInput) components.findComponent("phone")).setValue("");
                } else {
                    message = new FacesMessage("Wrong e-mail format!");
                    logger.warn("Wrong e-mail(" + mail + ") format!");
                    ((UIInput) components.findComponent("mail")).setValue("");
                }
            } else if (!password.isEmpty() && !confirmPassword.isEmpty() && !password.equals(confirmPassword)) {
                message = new FacesMessage("Passwords must be the same!");
                logger.warn("Passwords must be the same!");
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
