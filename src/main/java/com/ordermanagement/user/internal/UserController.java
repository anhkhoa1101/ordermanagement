// user/internal/UserController.java
package com.ordermanagement.user.internal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    public record CreateUserRequest(@NotBlank @Email String email, @NotBlank String fullName) {}

    public record UserResponse(UUID id, String email, String fullName, boolean active) {
        static UserResponse from(User u) {
            return new UserResponse(u.getId(), u.getEmail(), u.getFullName(), u.isActive());
        }
    }

    private final UserService service;

    UserController(UserService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody CreateUserRequest r) {
        return UserResponse.from(service.create(r.email(), r.fullName()));
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable UUID id) {
        return UserResponse.from(service.get(id));
    }
}