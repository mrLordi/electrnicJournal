package managedBeans.director;

import beans.DirectorBean;
import beans.SchoolBean;
import entities.Director;
import entities.School;
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

/**
 * Created by win10 on 27.11.2016.
 */

@ManagedBean(name = "schoolBean", eager = true)
@SessionScoped
public class SchoolController {

    private final static Logger logger = Logger.getLogger(SchoolController.class);

    @EJB
    private SchoolBean schoolBean;

    @EJB
    private DirectorBean directorBean;

    @ManagedProperty(value = "#{directorBean}")
    private DirectorController directorController;

    private String name;
    private String address;
    private String phone;
    private String information;

    private School school;

    private boolean addSchool;
    private boolean editSchool;

    private void init() {
        addSchool = false;
        editSchool = false;
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

    public String toHomePage() {
        init();

        return "/director/directorIndex?faces-redirect=true";
    }

    public String toAddSchool() {
        addSchool = true;

        name = "";
        address = "";
        phone = "";
        information = "";
        return "/director/school?faces-redirect=true";
    }

    public String cancelCreateSchool() {
        addSchool = false;
        return "/director/school?faces-redirect=true";
    }

    public String cancelEditSchool() {
        editSchool = false;
        return "/director/school?faces-redirect=true";
    }

    public String createSchool() {
        Director director = directorController.getDirector();

        School school = new School(name, phone, address, information, director);
        School createdSchool = schoolBean.add(school);

        director.setSchool(createdSchool);
        directorBean.update(director);
        return "/director/school?faces-redirect=true";
    }

    public String toEditSchool() {
        editSchool = true;
        return "/director/school?faces-redirect=true";
    }

    public String editSchoolInformation() {
        schoolBean.update(school);
        editSchool = false;
        return "/director/school?faces-redirect=true";
    }

    public void validateForm(ComponentSystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();

        String name = Helper.getInformation("name", components);
        String address = Helper.getInformation("address", components);
        String phone = Helper.getInformation("phone", components);
        String information = Helper.getInformation("information", components);


        FacesMessage message = null;
        if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || information.isEmpty()) {
            message = new FacesMessage("Fill in all required fields!");
            logger.warn("All required fields must be filled!");
        } else if (!Helper.checkPhone(phone)) {
            message = new FacesMessage("Wrong phone number format!");
            logger.warn("Wrong phone number(" + phone + ") format!");
            ((UIInput) components.findComponent("phone")).setValue("");
        }
        if (message != null) {
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(components.getClientId(), message);
            facesContext.renderResponse();
        }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public boolean isAddSchool() {
        return addSchool;
    }

    public void setAddSchool(boolean addSchool) {
        this.addSchool = addSchool;
    }

    public boolean isEditSchool() {
        return editSchool;
    }

    public void setEditSchool(boolean editSchool) {
        this.editSchool = editSchool;
    }
}
