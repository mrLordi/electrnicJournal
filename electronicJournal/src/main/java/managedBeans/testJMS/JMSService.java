package managedBeans.testJMS;

import org.apache.log4j.Logger;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Topic;

/**
 * Created by win10 on 07.12.2016.
 */
@ManagedBean(name = "jmsService", eager = true)
@SessionScoped
public class JMSService {
    private static final Logger logger = Logger.getLogger(JMSService.class);
    @Resource(lookup = "java:jboss/exported/jms/topic/electronicJournal")
//    @Resource(lookup = "java:/JmsTopic")
    private Topic topic;

    @Inject
    @JMSConnectionFactory("java:jboss/electonicJournalTopicFactory")
    private JMSContext context;

    private String date;
    private String time;
    private String reason;

    public String toSendMessage() {
        return "/director/sendMessage?faces-redirect=true";
    }

    public String sendMessage() {
        String message = (new StringBuilder(date).append('\n').append(time).append('\n').append(reason)).toString();
        logger.info("Sending message: " + message);
        context.createProducer().send(topic, message);
        setDate("");
        setTime("");
        setReason("");
        return "/director/directorIndex?faces-redirect=true";
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
