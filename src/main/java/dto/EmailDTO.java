package dto;

public class EmailDTO {

    private final String email;
    private final String message;

    public EmailDTO(String email, String message) {
        this.email = email;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }
}
