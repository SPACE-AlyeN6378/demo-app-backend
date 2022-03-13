package com.dsi.demoappbackend.registration;

import com.dsi.demoappbackend.email.InvalidEmailException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.cert.CertificateExpiredException;

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request) throws InvalidEmailException {
        return registrationService.register(request);
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) throws CertificateExpiredException {
        return registrationService.confirmToken(token);
    }

    @PostMapping(path = "new_token/{user_id}")
    public String newToken(@PathVariable("user_id") Long id) {
        return registrationService.newToken(id);
    }



}
