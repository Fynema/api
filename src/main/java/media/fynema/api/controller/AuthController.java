package media.fynema.api.controller;

import lombok.RequiredArgsConstructor;
import media.fynema.api.dto.requests.ChangePasswordRequestDTO;
import media.fynema.api.dto.requests.LoginRequestDTO;
import media.fynema.api.dto.requests.RefreshTokenRequestDTO;
import media.fynema.api.dto.responses.ChangePasswordResponseDTO;
import media.fynema.api.dto.responses.CreateTokensResponseDTO;
import media.fynema.api.model.User;
import media.fynema.api.repository.UserRepository;
import media.fynema.api.services.AuthService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
class AuthController {
    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/login")
    public CreateTokensResponseDTO login(@RequestBody LoginRequestDTO request) {
        User user = userRepository.findByUsername(request.username());
        if (user == null || authService.validateUserCredentials(user, request.password())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return this.authService.generateTokens(user);
    }

    @PostMapping("/refresh")
    public CreateTokensResponseDTO refresh(
            @RequestBody RefreshTokenRequestDTO refreshToken,
            @AuthenticationPrincipal User user
    ) {
        boolean validateRefreshToken = authService.validateRefreshToken(refreshToken.refreshToken());

        if (!validateRefreshToken) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        return authService.generateTokens(user);
    }

    @PostMapping("/password/change")
    public ChangePasswordResponseDTO changePassword(@RequestBody ChangePasswordRequestDTO request, @AuthenticationPrincipal User user) {
        return authService.changePassword(user, request);
    }
}
