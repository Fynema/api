package media.fynema.api.services;

import media.fynema.api.dto.CreateUserRequestDTO;
import media.fynema.api.dto.UserResponseDTO;
import media.fynema.api.model.User;
import media.fynema.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO createUser(CreateUserRequestDTO request) {
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(request.password()); // hash later

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
                user.getUsername()
        );
    }
}
