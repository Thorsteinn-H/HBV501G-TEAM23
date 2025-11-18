package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Services.Interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    /**
     * Lists all users. Admin only.
     *
     * @return a list of {@link UserDto.UserResponse} containing the mapped data to all users
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List users", description = "Lists all users with optional filtering and sorting. Admin only.")
    public List<UserDto.UserResponse> getAllUsers(
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String username,
        @RequestParam(required = false) String role,
        @RequestParam(required = false) Boolean active,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        try {
            return userService.findUsers(email, username, role, active, sortBy, order)
                .stream()
                .map(this::toResponse)
                .toList();
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving users", e);
        }
    }

    /**
     * Gets a single user by ID. Admin only.
     *
     * @param id the id of the user to find
     * @return a {@link UserDto.UserResponse} containing the mapped data
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID", description = "Admin only.")
    public ResponseEntity<UserDto.UserResponse> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null || !user.isActive()) {
            throw new EntityNotFoundException("User not found");
        }
        return ResponseEntity.ok(toResponse(user));
    }

    /**
     * Creates a new user. Admin only.
     *
     * @param request the {@link UserDto.CreateUserRequest} containing the user details
     * @return {@link ResponseEntity} with status 201 (CREATED) containing the created {@link UserDto.UserResponse}
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Create user",
        description = "Creates a new user. Admin only."
    )
    public ResponseEntity<UserDto.UserResponse> createUser(@Valid @RequestBody UserDto.CreateUserRequest request) {
        User newUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(newUser));
    }

    /**
     * Updates an existing user by ID. Admin only.
     *
     * @param id the ID of the user to update
     * @param request the {@link UserDto.PatchUserRequest} containing fields to update
     * @return {@link ResponseEntity} with status 200 (OK) containing the mapped {@link UserDto.UserResponse}
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user", description = "Updates a user's data. Admin only.")
    public ResponseEntity<UserDto.UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDto.PatchUserRequest request) {

        User updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(toResponse(updatedUser));
    }

    /**
     * Performs a soft delete on a user by marking them as inactive
     * and anonymizing their data. Admin only.
     *
     * @param id the ID of the user to deactivate
     * @return {@link ResponseEntity} with status 204 (NO_CONTENT) if the operation is successful
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deactivate user", description = "Marks user as inactive and anonymizes user data. Admin only.")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Maps a {@link User} entity to a {@link UserDto.UserResponse} DTO.
     * @param u user entity to map
     * @return mapped {@link UserDto.UserResponse}
     */
    private UserDto.UserResponse toResponse(User u) {
          return new UserDto.UserResponse(
                  u.getId(),
                  u.getEmail(),
                  u.getName(),
                  u.getGender(),
                  u.getRole(),
                  u.isActive(),
                  u.getCreatedAt(),
                  u.getProfileImage()
          );
      }
}



