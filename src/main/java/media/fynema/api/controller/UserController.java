package media.fynema.api.controller;

import media.fynema.api.dto.CreateUserRequestDTO;
import media.fynema.api.dto.UserResponseDTO;
import media.fynema.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserRequestDTO createUserRequestDTO) {
        UserResponseDTO response = this.userService.createUser(createUserRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> response = this.userService.getAllUsers();
        return ResponseEntity.ok(response);
    }
}
