package com.dsi.demoappbackend.appuser;

import com.dsi.demoappbackend.appuser.exceptions.BadCredentialsException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/client")
@AllArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping(path = "login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("pwd") String pwd) {

        try {
            return appUserService.loginCheck(email, pwd);
        }
        catch (BadCredentialsException e) {
            return e.getMessage();
        }
    }
}
