package sg.nus.iss.blog.model;

public class EmailDetails {

    private String recipient;
    private String subject;
    private String messageBody;

    public EmailDetails(String recipient, String subject, String messageBody) {
        this.recipient = recipient;
        this.subject = subject;
        this.messageBody = messageBody;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
