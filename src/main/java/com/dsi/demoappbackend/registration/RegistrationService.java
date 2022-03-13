package com.dsi.demoappbackend.registration;

import com.dsi.demoappbackend.appuser.AppUser;
import com.dsi.demoappbackend.appuser.AppUserRole;
import com.dsi.demoappbackend.appuser.AppUserService;
import com.dsi.demoappbackend.email.EmailSender;
import com.dsi.demoappbackend.email.EmailValidator;
import com.dsi.demoappbackend.email.InvalidEmailException;
import com.dsi.demoappbackend.registration.token.ConfirmationToken;
import com.dsi.demoappbackend.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        emailSender.send(
                request.getEmail(),
                buildEmail(request.getFirstName(), link));
        
        return token;
    }

    // Do that when your current token expires
    public String resendEmail(Long id) {
        String token = appUserService.makeNewToken(id);
        AppUser appUser = appUserService.getAppUser(id);

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        emailSender.send(
            appUser.getEmail(),
                buildEmail(appUser.getFirstName(), link));

        return token;
    }

    public String newToken(Long id) {
        return appUserService.makeNewToken(id);
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


        String confirmedHtml = "";
        try {

            File html_file = new File("/Users/aly/IdeaProjects/demo-app-backend/src/main/resources/static/confirmpage.html");
            // File html_file = new File("/home/dsi/IdeaProjects/ATSProject/src/main/html/confirmpage.html");
            Scanner myReader = new Scanner(html_file);
            while (myReader.hasNextLine()) {
                confirmedHtml += myReader.nextLine();
            }
            myReader.close();

        } catch (FileNotFoundException e) {
            confirmedHtml += "confirmed";
        }

        return confirmedHtml;
    }

    private String buildEmail(String name, String link) {

        String emailBody = "";
        try {
            // TODO: Consider changing
            File html_file = new File("/Users/aly/IdeaProjects/demo-app-backend/src/main/resources/static/emailbody.html");
            // File html_file = new File("/home/dsi/IdeaProjects/ATSProject/src/main/html/emailbody.html");
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
