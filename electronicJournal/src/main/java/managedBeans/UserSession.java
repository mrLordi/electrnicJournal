package managedBeans;

import org.apache.log4j.Logger;
import utils.Helper;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by win10 on 23.11.2016.
 */
@ManagedBean(name = "userSession", eager = true)
@SessionScoped
public class UserSession {
    private static final Logger logger = Logger.getLogger(UserSession.class);

    private String username;
    private String password;
    private boolean isLogin;

    public String login() {
        if (isLogin) {
            logout();
        }

        HttpServletRequest request =
                (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try {
            request.login(username, password);

            isLogin = true;
            if (request.isUserInRole("DIRECTOR")) {
                return "/director/directorIndex?faces-redirect=true";
            } else if (request.isUserInRole("TEACHER")) {
                return "/teacher/teacherIndex?faces-redirect=true";
            } else if (request.isUserInRole("STUDENT")) {
                return "/student/studentIndex?faces-redirect=true";
            } else if (request.isUserInRole("CLASS_TEACHER")) {
                return "/classTeacher/classTeacherIndex?faces-redirect=true";
            }

        } catch (ServletException e) {
            logger.error("Error authorization user with login=" + username);
            return "/error";
        } finally {
            username = "";
            password = "";
        }

        logger.warn("Unknown user role!");
        logout();
        return "/error";
    }

    public String logout() {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        logger.info("Invalidate session for user: " + Helper.getCurrentUser());
        session.invalidate();
        isLogin = false;

        return "/index?faces-redirect=true";
    }

    public String toUserPage() {
        HttpServletRequest request =
                (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        if (request.isUserInRole("DIRECTOR")) {
            return "/director/directorIndex?faces-redirect=true";
        } else if (request.isUserInRole("TEACHER")) {
            return "/teacher/teacherIndex?faces-redirect=true";
        } else if (request.isUserInRole("CLASS_TEACHER")) {
            return "/classTeacher/classTeacherIndex?faces-redirect=true";
        } else if (request.isUserInRole("STUDENT")) {
            return "/student/studentIndex?faces-redirect=true";
        }

        logger.warn("Unknown user role!");
        logout();
        return "/error";
    }

    public void validateForm(ComponentSystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();

        String login = Helper.getInformation("login", components);
        String password = Helper.getInformation("password", components);

        if (login.isEmpty() || password.isEmpty()) {
            FacesMessage message = new FacesMessage("Fill in 'login' and 'password' required fields!");
            logger.warn("All required fields must be filled!");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(components.getClientId(), message);
            facesContext.renderResponse();
        }
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
