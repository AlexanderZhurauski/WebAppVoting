package dao.entity;

public class EmailEntity {

    private int id;
    private String recipient;
    private String topic;
    private String textMessage;
    private int departures;
    private EmailStatus status;

    public EmailEntity(String recipient, String topic, String textMessage,
                       int departures, EmailStatus status) {
        this.recipient = recipient;
        this.topic = topic;
        this.textMessage = textMessage;
        this.departures = departures;
        this.status = status;
    }

    public EmailEntity(int id, String recipient, String topic,
                       String textMessage, int departures, EmailStatus status) {
        this.id = id;
        this.recipient = recipient;
        this.topic = topic;
        this.textMessage = textMessage;
        this.departures = departures;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getTopic() {
        return topic;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public int getDepartures() {
        return departures;
    }

    public EmailStatus getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public void setDepartures(int departures) {
        this.departures = departures;
    }

    public void setStatus(EmailStatus status) {
        this.status = status;
    }
}