package media.fynema.api.controller;

import lombok.RequiredArgsConstructor;
import media.fynema.api.dto.LoginRequestDTO;
import media.fynema.api.dto.LoginResponseDTO;
import media.fynema.api.model.User;
import media.fynema.api.repository.UserRepository;
import media.fynema.api.services.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        User user = userRepository.findByUsername(request.username());
        if (user == null || !passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getId());
        return new LoginResponseDTO(token);
    }
}
