package media.fynema.api.services;

import lombok.RequiredArgsConstructor;
import media.fynema.api.dto.requests.ChangePasswordRequestDTO;
import media.fynema.api.dto.responses.ChangePasswordResponseDTO;
import media.fynema.api.dto.responses.CreateTokensResponseDTO;
import media.fynema.api.model.User;
import media.fynema.api.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public CreateTokensResponseDTO generateTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user.getId());
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        return new CreateTokensResponseDTO(accessToken, refreshToken);
    }

    public boolean validateUserCredentials(User user, String rawPassword) {
        return !passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public ChangePasswordResponseDTO changePassword(User user, ChangePasswordRequestDTO request) {
        if (validateUserCredentials(user, request.oldPassword())) {
            throw new BadCredentialsException("Invalid old password");
        }
        String newPasswordHashed = passwordEncoder.encode(request.newPassword());
        user.setPassword(newPasswordHashed);
        userRepository.save(user);
        return new ChangePasswordResponseDTO(true);
    }

    public boolean validateAccessToken(String accessToken) {
        return jwtService.validateAccessToken(accessToken);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return jwtService.validateRefreshToken(refreshToken);
    }
}
