package com.dsi.demoappbackend.registration;

import com.dsi.demoappbackend.appuser.AppUser;
import com.dsi.demoappbackend.appuser.AppUserRepository;
import com.dsi.demoappbackend.appuser.AppUserRole;
import com.dsi.demoappbackend.appuser.AppUserService;
import com.dsi.demoappbackend.email.EmailSender;
import com.dsi.demoappbackend.email.EmailValidator;
import com.dsi.demoappbackend.email.InvalidEmailException;
import com.dsi.demoappbackend.registration.token.ConfirmationToken;
import com.dsi.demoappbackend.registration.token.ConfirmationTokenRepository;
import com.dsi.demoappbackend.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.cert.CertificateExpiredException;
import java.time.LocalDateTime;
import java.util.Scanner;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final EmailSender emailResender;

    public String register(RegistrationRequest request) throws InvalidEmailException {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            throw new InvalidEmailException("email not valid");
        }

        String token = appUserService.signUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );

        String link = "http://localhost:3000/conf/" + token;
        emailSender.send(
                request.getEmail(),
                buildEmail(request.getFirstName(), link));
        
        return token;
    }

    // Do that when your current token expires
    public String resendEmail(String expiredToken) {

        if (confirmationTokenService.getToken(expiredToken).isEmpty()) {
            throw new EntityNotFoundException("token not found");
        }

        AppUser appUser = confirmationTokenService.getToken(expiredToken).get().getAppUser();
        String token = appUserService.makeNewToken(appUser);

        String link = "http://localhost:3000/conf/" + token;
        emailResender.send(
            appUser.getEmail(),
                buildEmail(appUser.getFirstName(), link));

        return token;
    }


    @Transactional
    public String confirmToken(String token) throws CertificateExpiredException {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new EntityNotFoundException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new RuntimeException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new CertificateExpiredException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
            confirmationToken.getAppUser().getEmail());

        return "confirmed";
    }

    private String buildEmail(String name, String link) {

        String emailBody = "";
        try {
            File html_file = new File("/home/dsi/IdeaProjects/demo-app-backend/src/main/resources/static/emailbody.html");
            Scanner myReader = new Scanner(html_file);
            while (myReader.hasNextLine()) {
                emailBody += myReader.nextLine();
            }
            myReader.close();

            emailBody = emailBody.replaceAll("\\{FIRST_NAME\\}", name).replaceAll("\\{LINK\\}", link);

        } catch (FileNotFoundException e) {
            emailBody += "File not found";
        }

        return emailBody;

    }
}
