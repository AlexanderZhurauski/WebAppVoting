package web.controllers;

import dto.EmailDTO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.VoteService;
import service.api.ISenderService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@SessionAttributes
        ({EmailVerificationController.VERIFICATION_KEY_ATTRIBUTE,
                EmailVerificationController.AUTHENTICATION_ATTRIBUTE})
public class EmailVerificationController {

    static final String VERIFICATION_KEY_ATTRIBUTE = "key";
    static final String AUTHENTICATION_ATTRIBUTE = "isAuthenticated";
    private final ISenderService service;

    public EmailVerificationController(ISenderService service) {
        this.service = service;
    }

    @PostMapping("/verification")
    public void createVerificationKey(Model model,
                                      HttpServletRequest req,
                                      @RequestParam String email) {
        if (email == null || email.isBlank() || !email.matches(VoteService.EMAIL_PATTERN)) {
            throw new IllegalArgumentException("Invalid email provided!");
        }
        UUID key = UUID.randomUUID();
        String verificationLink =  req.getRequestURL().toString() + "?"
                + VERIFICATION_KEY_ATTRIBUTE + "=" + key;
        model.addAttribute(VERIFICATION_KEY_ATTRIBUTE, key.toString());
        this.service.verifyEmail(new EmailDTO(email,
                "<a href=" + verificationLink + ">Confirm email!</a>"));
    }

    @GetMapping("/verification")
    public void verifyEmail(Model model, @RequestParam String keyFromEmail) {
        if (keyFromEmail == null || keyFromEmail.isBlank()) {
            throw new IllegalArgumentException("No key has been provided!");
        }
        String keyFromServer = (String) model.getAttribute(VERIFICATION_KEY_ATTRIBUTE);
        if (keyFromEmail == null || keyFromEmail.isBlank()) {
            throw new IllegalArgumentException("The user has no verification key " +
                    "associated with the session!");
        }
        model.addAttribute(AUTHENTICATION_ATTRIBUTE, keyFromServer.equals(keyFromEmail));
    }
}
