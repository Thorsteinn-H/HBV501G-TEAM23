package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import is.hi.hbv501gteam23.Persistence.Entities.Favorites;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.dto.LoginDto;
import is.hi.hbv501gteam23.Persistence.dto.MatchDto;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Persistence.dto.VenueDto;
import is.hi.hbv501gteam23.Security.CustomUserDetails;
import is.hi.hbv501gteam23.Security.JwtTokenProvider;
import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller that exposes read/write operations for users
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Logs inn a user with the given email and password.
     * @param req a {@link LoginDto.LoginResponse} containing the users log in information
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate and return JWT token")
    public ResponseEntity<LoginDto.LoginResponse> login(@Valid @RequestBody LoginDto.LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication.getName());

        User user = authService.findByEmail(authentication.getName());
        return ResponseEntity.ok(new LoginDto.LoginResponse(token, toResponse(user)));
    }

    /**
     * Get current logged inn users details
     * @param userDetails the logged inn user's details
     * @return a {@link UserDto.UserResponse} containing the mapped data
     */
    @GetMapping("/loggedin")
    @Operation(summary = "Get currently logged in user")
    public ResponseEntity<UserDto.UserResponse> loggedIn(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = authService.findByEmail(userDetails.getUsername());
        if (user == null || !user.isActive()) throw new EntityNotFoundException("User not found");
        return ResponseEntity.ok(toResponse(user));
    }

    /**
     * Registers a new user with the provided details.
     *
     * @param request  a {@link UserDto.CreateUserRequest} containing the users information
     * @return a {@link UserDto.UserResponse} containing the mapped data
     */
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Creates a new user account")
    @ApiResponse(responseCode = "201", description = "User created")
    public ResponseEntity<UserDto.UserResponse> registerUser(@Valid @RequestBody UserDto.CreateUserRequest request) {
        User created = authService.registerUser(request);
        return ResponseEntity.ok(toResponse(created));
    }

    /**
     * Retrieves all users who are currently active.
     *
     * @return a list of {@link UserDto.UserResponse} containing the mapped data to all active users
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Returns all non-deleted users")
    @ApiResponse(responseCode = "200", description = "List of all users")
    public List<UserDto.UserResponse> getAllUsers() {
        return authService.getAllActiveUsers().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Finds a user by their id.
     *
     * @param id the id of the user to find
     * @return a {@link UserDto.UserResponse} containing the mapped data
     */
    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID")
    @ApiResponse(responseCode = "200", description = "User found")
    public ResponseEntity<UserDto.UserResponse> getUser(@PathVariable Long id) {
        User user = authService.findById(id);
        if (user == null || !user.isActive()) {
            throw new EntityNotFoundException("User not found");
        }
        return ResponseEntity.ok(toResponse(user));
    }

    /**
     * Performs a soft delete a user by marking them as inactive
     * instead of removing them permanently.
     *
     * @param id  the id of the user to delete
     * @return a {@link UserDto.UserResponse} containing the mapped data
     */
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user", description = "Marks user as inactive and anonymizes user data")
    @ApiResponse(responseCode = "200", description = "User successfully deleted")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        authService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update the users password
     * @param userDetails the logged inn user's details
     * @param request a {@link UserDto.updateGender} containing the new gender information
     * @return a {@link UserDto.UserResponse} containing the mapped data
     */
    @PostMapping("/users/update_password")
    @Operation(summary = "Update the current user's password")
    public ResponseEntity<UserDto.UserResponse> updatePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserDto.updatePassword request
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = authService.findByEmail(userDetails.getUsername());
        if (user == null || !user.isActive()) {
            throw new EntityNotFoundException("User not found");
        }

        User updatedUser = authService.updatePassword(user, request);
        return ResponseEntity.ok(toResponse(updatedUser));
    }

    /**
     * Update the users username
     * @param userDetails the logged inn user's details
     * @param request a {@link UserDto.updateGender} object containing the new username information
     * @return a {@link UserDto.UserResponse} containing the mapped data
     */
    @PostMapping("/users/update_username")
    @Operation(summary = "Update the current user's username")
    public ResponseEntity<UserDto.UserResponse> updateUsername(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserDto.updateUsername request
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = authService.findByEmail(userDetails.getUsername());
        if (user == null || !user.isActive()) {
            throw new EntityNotFoundException("User not found");
        }

        User updatedUser = authService.updateUsername(user, request);
        return ResponseEntity.ok(toResponse(updatedUser));
    }

    /**
     * Update the users gender
     * @param userDetails the logged inn user's details
     * @param request a {@link UserDto.updateGender} object containing the new gender information
     * @return a {@link UserDto.UserResponse} containing the mapped data
     */
    @PostMapping("/users/update_gender")
    @Operation(summary = "Update the current user's gender")
    public ResponseEntity<UserDto.UserResponse> updateGender(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserDto.updateGender request
    ) {

        List<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");
        genders.add("Other");
        genders.add("Prefer not to say");

        if (!genders.contains(request.newGender())) {
            throw new EntityNotFoundException("Not a valid gender");
        }

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = authService.findByEmail(userDetails.getUsername());
        if (user == null || !user.isActive()) {
            throw new EntityNotFoundException("User not found");
        }

        User updatedUser = authService.updateGender(user, request);
        return ResponseEntity.ok(toResponse(updatedUser));
    }

    /**
     * Sets the logged inn users image
     * @param userDetails the logged inn user's details
     * @param file the image file
     * @return a {@link ResponseEntity} containing the user's image as a byte array and
     * the image type
     * @throws IOException if an error occurs while reading or storing the file
     */
    @PostMapping("/users/upload_image")
    @Operation(summary = "Update/Add the users profile picture")
    public ResponseEntity<UserDto.UserResponse> uploadImage (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = authService.findByEmail(userDetails.getUsername());

        if (user == null || !user.isActive()) {
            throw new EntityNotFoundException("User not found");
        }
        String fileType = file.getContentType();

        if (fileType != null && fileType.startsWith("image")) {
            User updatedUser = authService.uploadImage(user, file, fileType);
            return ResponseEntity.ok(toResponse(updatedUser));

        } else {
            throw new EntityNotFoundException("file type incorrect");
        }

    }

    /**
     * Retrieves the logged inn user's image.
     *
     * @param userDetails the logged inn user's details
     * @return a {@link ResponseEntity} containing the user's image as a byte array and
     * the image type
     *
     */
    @GetMapping("/users/get_image")
    @Operation(summary = "Get users profile picture")
    public ResponseEntity<?> getUserProfilePicture(
            @AuthenticationPrincipal CustomUserDetails userDetails){

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = authService.findByEmail(userDetails.getUsername());

        if (user == null || !user.isActive()) {
            throw new EntityNotFoundException("User not found");
        }

        byte[] image = authService.getImage(user);
        String fileType = authService.getImageType(user);

        return ResponseEntity.ok().header("Content-Type", fileType)
                .body(image);

    }


    /**
     * Deletes the logged in users image
     *
     * @param userDetails the logged inn user's details
     * @return a {@link ResponseEntity} containing the user's details
     *
     */
    @DeleteMapping("/delete_image")
    @Operation(summary = "Delete users profile picture")
    public ResponseEntity<?> deleteUserProfilePicture(@AuthenticationPrincipal CustomUserDetails userDetails){
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = authService.findByEmail(userDetails.getUsername());

        if (user == null || !user.isActive()) {
            throw new EntityNotFoundException("User not found");
        }

        user.setImageType(null);
        user.setImage(null);

        return ResponseEntity.ok(toResponse(user));
    }


    /**
     * Maps a {@link User} entity to a {@link UserDto.UserResponse} DTO.
     * @param u user entity
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
                  u.getImage(),
                  u.getImageType()
          );
      }
}



