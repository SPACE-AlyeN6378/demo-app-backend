package com.dsi.demoappbackend.appuser;

import com.dsi.demoappbackend.appuser.exceptions.BadCredentialsException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/client")
@AllArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping(path = "login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("pwd") String pwd) throws BadCredentialsException {

        return appUserService.loginCheck(email, pwd);
    }

    @GetMapping(path = "get/{email}")
    public AppUser getUser(@PathVariable String email) {
        return appUserService.getUserByEmail(email);
    }
}
