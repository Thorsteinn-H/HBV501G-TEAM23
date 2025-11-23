package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Services.Interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST controller that exposes read/write operations for {@link User} resources.
 * <p>
 * All endpoints in this controller are restricted to administrators.
 * Base path is /users
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "User", description = "Restricted to admin only")
@Transactional
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    /**
     * Lists all users with optional filtering and sorting.
     *
     * @param filter   filter and sorting parameters
     * @return a list of {@link UserDto.UserResponse}
     */
    @GetMapping
    @Operation(summary = "List users", description = "Lists all users with optional filtering and sorting. Admin only.")
    public ResponseEntity<List<UserDto.UserResponse>> listUsers(
            @ParameterObject @ModelAttribute UserDto.UserFilter filter
    ) {
        List<UserDto.UserResponse> users = userService.listUsers(filter)
            .stream()
            .map(this::toResponse)
            .toList();
        return ResponseEntity.ok(users);
    }

    /**
     * Gets a single user by ID.
     *
     * @param id the id of the user to find
     * @return a {@link UserDto.UserResponse} containing the mapped data
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Admin only.")
    public ResponseEntity<UserDto.UserResponse> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null || !user.isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return ResponseEntity.ok(toResponse(user));
    }

    /**
     * Creates a new user.
     *
     * @param request the {@link UserDto.CreateUserRequest} containing the user details
     * @return {@link ResponseEntity} with status 201 (CREATED) containing the created {@link UserDto.UserResponse}
     */
    @PostMapping
    @Operation(summary = "Create user", description = "Creates a new user. Admin only.")
    public ResponseEntity<UserDto.UserResponse> createUser(@Valid @RequestBody UserDto.CreateUserRequest request) {
        User newUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(newUser));
    }

    /**
     * Updates an existing user by ID.
     *
     * @param id the ID of the user to update
     * @param request the {@link UserDto.PatchUserRequest} containing fields to update
     * @return {@link ResponseEntity} with status 200 (OK) containing the mapped {@link UserDto.UserResponse}
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates a user's data. Admin only.")
    public ResponseEntity<UserDto.UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDto.PatchUserRequest request) {
        User updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(toResponse(updatedUser));
    }

    /**
     * Performs a soft delete by marking user as inactive.
     *
     * @param id the ID of the user to deactivate
     * @return {@link ResponseEntity} with status 204 (NO_CONTENT) if the operation is successful
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate user", description = "Marks user as inactive and anonymizes user data. Admin only.")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Maps a {@link User} entity to a {@link UserDto.UserResponse} DTO.
     *
     * @param user entity to map
     * @return mapped {@link UserDto.UserResponse}
     */
    private UserDto.UserResponse toResponse(User user) {
        String profileImageUrl = user.getProfileImage() != null ? "/profile/avatar" : null;
        return new UserDto.UserResponse(
          user.getId(),
          user.getEmail(),
          user.getName(),
          user.getGender(),
          user.getRole(),
          user.isActive(),
          user.getCreatedAt(),
          profileImageUrl
        );
    }
}
