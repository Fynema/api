package media.fynema.api.services;

import lombok.RequiredArgsConstructor;
import media.fynema.api.dto.requests.CreateUserRequestDTO;
import media.fynema.api.dto.responses.UserResponseDTO;
import media.fynema.api.model.User;
import media.fynema.api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(CreateUserRequestDTO request) {
        User user = new User();
        user.setUsername(request.username());

        String passwordHashed = passwordEncoder.encode(request.password());
        user.setPassword(passwordHashed);

        User savedUser = userRepository.save(user);
        return toResponseDTO(savedUser);
    }

    public UserResponseDTO getUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null; // or throw an exception
        }
        return toResponseDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::toResponseDTO).toList();
    }

    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername()
        );
    }
}
