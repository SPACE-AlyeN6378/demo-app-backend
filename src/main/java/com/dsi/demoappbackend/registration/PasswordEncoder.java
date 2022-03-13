package com.dsi.demoappbackend.registration;

import org.springframework.context.annotation.Configuration;
import java.util.Base64;

@Configuration
public class PasswordEncoder {

    public static String encode(String pwd) {
        return Base64.getEncoder().encodeToString(pwd.getBytes());
    }

    private final static String USER_NOT_FOUND_MSG = "user with %s not found";

}
