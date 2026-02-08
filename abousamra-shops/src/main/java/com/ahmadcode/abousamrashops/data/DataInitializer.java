package com.ahmadcode.abousamrashops.data;

import com.ahmadcode.abousamrashops.model.Role;
import com.ahmadcode.abousamrashops.model.User;
import com.ahmadcode.abousamrashops.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

@Transactional
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN" , "ROLE_USER");
        createDefaultUsersIfNotExists();
        createDefaultRolesIfNotExists(defaultRoles);
        createDefaultAdminIfNotExists();
    }

    private void createDefaultUsersIfNotExists() {
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        for (int i = 1; i <= 5; i++) {
            String defaultEmail = "user" + i + "@gmail.com";

            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }

            User user = new User();
            user.setEmail(defaultEmail);
            user.setFirstName("The User");
            user.setLastName("User " + i);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("Default vet user"+ i +" created successfully");
        }
    }

    private void createDefaultAdminIfNotExists() {
        Role userRole = roleRepository.findByName("ROLE_ADMIN").get();
        for (int i = 1; i <= 2; i++) {
            String defaultEmail = "admin" + i + "@gmail.com";

            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }

            User user = new User();
            user.setEmail(defaultEmail);
            user.setFirstName("Admin");
            user.setLastName("Admin " + i);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("Default admin user" + i + " created successfully");
        }
    }

    private void createDefaultRolesIfNotExists(Set<String> roles){
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role::new).forEach(roleRepository::save);
    }

}

