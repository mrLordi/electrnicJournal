package managedBeans.testJMS;

import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import managedBeans.UserSession;
import org.apache.log4j.Logger;
import sun.plugin2.message.Message;
import utils.Helper;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.jms.*;
import java.util.Random;

/**
 * Created by win10 on 07.12.2016.
 */
@ManagedBean(name = "jmsReceiver", eager = true)
@SessionScoped
//@MessageDriven(activationConfig = {
//        @ActivationConfigProperty(propertyName="destinationLookup", propertyValue="JmsTopic"),
//        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
//})

public class JMSReceiver /*implements MessageListener*/ {
    private static final Logger logger = Logger.getLogger(JMSReceiver.class);

    @Resource(lookup = "java:jboss/exported/jms/topic/electronicJournal")
//    @Resource(lookup = "java:/JmsTopic")
    private Topic topic;

    @Inject
    @JMSConnectionFactory("java:jboss/electonicJournalTopicFactory")
    private JMSContext context;

    private String date;
    private String time;
    private String reason;

    public String receiveMessage() {
        String login = Helper.getCurrentUser();
        JMSConsumer consumer = context.createSharedDurableConsumer(topic, login);
        String message =  consumer.receiveBody(String.class);
        String[] strings = message.split("\n");
        date = strings[0];
        time = strings[1];
        reason = strings[2];

        return "/messagePage?redirect?faces-redirect=true";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
