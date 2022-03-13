package com.dsi.demoappbackend.appuser;

import com.dsi.demoappbackend.appuser.exceptions.BadCredentialsException;
import com.dsi.demoappbackend.registration.PasswordEncoder;
import com.dsi.demoappbackend.registration.token.ConfirmationToken;
import com.dsi.demoappbackend.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";

    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenService confirmationTokenService;

    public AppUser getUserByEmail(String email) {

        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String loginCheck(String email, String password) throws BadCredentialsException {

        Optional<AppUser> appUserOptional = Optional.ofNullable(appUserRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid E-mail ID")));

        String correctPwd = appUserOptional.get().getPassword();
        String givenPwd = PasswordEncoder.encode(password);

        if (!givenPwd.equals(correctPwd)) {
            throw new BadCredentialsException("Incorrect Password");
        }

        return "LOGIN SUCCESSFUL!";
    }

    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();

        if (userExists) {
            throw new IllegalStateException("email already taken");
        }

        String encodedPassword = PasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public AppUser getAppUser(Long appUserId) {
        if (!appUserRepository.existsById(appUserId)) {
            throw new EntityNotFoundException("The user with id "+appUserId+" does not exist.");
        }

        return appUserRepository.getById(appUserId);
    }

    public String makeNewToken(Long appUserId) {

        if (!appUserRepository.existsById(appUserId)) {
            throw new EntityNotFoundException("The user with id "+appUserId+" does not exist.");
        }

        AppUser appUser = appUserRepository.getById(appUserId);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), appUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    public List<AppUser> getUsers() {
        return appUserRepository.findAll();
    }
}
