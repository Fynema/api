package media.fynema.api.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import media.fynema.api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = Logger.getLogger(DataInitializer.class.getName());

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            var user = new media.fynema.api.model.User();
            user.setUsername("admin");

            String generatedPassword = generateRandomPassword();

            user.setPassword(passwordEncoder.encode(generatedPassword));
            userRepository.save(user);
            logger.log(Level.INFO, "\n\nDefault admin user created with username 'admin'. \nPlease change the password after first login, \n\nthe password is: " + generatedPassword + "\n" );
        }
    }

    private String generateRandomPassword() {
        int length = 16;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }
}
