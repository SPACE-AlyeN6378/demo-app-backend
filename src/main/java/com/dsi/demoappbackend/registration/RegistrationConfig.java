package com.dsi.demoappbackend.registration;

import com.dsi.demoappbackend.appuser.AppUser;
import com.dsi.demoappbackend.appuser.AppUserRepository;
import com.dsi.demoappbackend.appuser.AppUserRole;
import com.dsi.demoappbackend.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@AllArgsConstructor
public class RegistrationConfig {

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner commandLineRunner(AppUserRepository repository) {
        return args -> {
            AppUser admin = new AppUser(
                    "Aly",
                    "Mooltazeem",
                    "alymooltazeem@gmail.com",
                    "helloworld",
                    AppUserRole.ADMIN
            );

            String encodedPassword = PasswordEncoder.encode(admin.getPassword());
            admin.setPassword(encodedPassword);
            admin.setEnabled(true);

            repository.saveAll(List.of(admin));
        };
    }
}
