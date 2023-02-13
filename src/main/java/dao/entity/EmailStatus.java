package dao.entity;

public enum EmailStatus {

    WAITING("email waiting to be sent"),
    SENT("email is sent"),
    ERROR("email sending error"),
    SUCCESS("email sent successfully");

    private final String description;

    EmailStatus(String description) {
        this.description = description;
    }

    public static EmailStatus getEmailStatus(String description) {
        for (EmailStatus emailStatus: EmailStatus.values()){
            if (emailStatus.description.equals(description)) {
                return emailStatus;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return description;
    }
}