package web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import service.api.ISenderService;

@RestController
public class EmailVerificationController {

    private static final String VERIFICATION_KEY_ATTRIBUTE = "key";
    private static final String AUTHENTICATION_ATTRIBUTE = "isAuthenticated";
    private final ISenderService service;

    public EmailVerificationController(ISenderService service) {
        this.service = service;
    }

    @PostMapping("/verification")
    public void createVerificationKey() {}

    @GetMapping("/verification")
    public void verifyEmail() {}
}
