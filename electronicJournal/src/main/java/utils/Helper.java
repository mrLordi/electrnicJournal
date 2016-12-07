package utils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

/**
 * Created by win10 on 07.12.2016.
 */
public class Helper {

    public static String getInformation(String component, UIComponent components) {
        Object input = ((UIInput) components.findComponent(component)).getLocalValue();
        return input != null ? input.toString() : "";
    }

    public static boolean checkPhone(String phone) {
        return ((phone.matches("^\\+[\\(\\-]?(\\d[\\(\\)\\-]?){11}\\d$") || phone.matches("^\\(?(\\d[\\-\\(\\)]?){9}\\d$"))
                && phone.matches("[\\+]?\\d*(\\(\\d{3}\\))?\\d*\\-?\\d*\\-?\\d*\\d$"));

    }

    public static boolean checkMail(String mail) {
        return mail.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    }

    public static String getCurrentUser() {
        return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }

}
