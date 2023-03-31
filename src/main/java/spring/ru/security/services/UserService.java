package spring.ru.security.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.ru.security.models.User;
import spring.ru.security.repositories.RoleRepository;
import spring.ru.security.repositories.UserRepository;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean createUser(User user, String role) {

        var email = user.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            return false;
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList(
                roleRepository.findByName(role)));
        userRepository.save(user);
        log.info("Saving new User with email: {}; role: {}", email, role);

        return true;
    }

    public List<User> all() {
        return userRepository.findAll();
    }

    public void banUser(Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setActive(!user.isActive());
            log.info("Бан пользователя id: {}; email: {}; active: {};", user.getId(), user.getEmail(), user.isActive());
            userRepository.save(user);
        }
    }

    public void setRoles(User user, String[] roles) {
            user.getRoles().clear();
            for (String role : roles) {
                user.getRoles().add(roleRepository.findByName(role));
            }
            log.info("Права пользователя id: {}; email: {}; new roles: {};", user.getId(), user.getEmail(), user.getRoleList());
            userRepository.save(user);
    }
    public User getUserByPrincipal(Principal principal) {
        return principal == null
                ? null
                : userRepository.findByEmail(principal.getName())
                .orElseThrow();
    }
}
