package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Security.CustomUserDetails;
import is.hi.hbv501gteam23.Services.Interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/profile")
@Tag(name = "Profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;

    /**
     * Get profile of currently logged-in user
     * @param userDetails the logged-in user's profile information
     * @return a {@link UserDto.UserResponse} containing the mapped data
     */
    @GetMapping
    @Operation(summary = "Get profile", description = "Get profile of currently logged-in user. Must be logged in.")
    public ResponseEntity<UserDto.UserResponse> loggedIn(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.findByEmail(userDetails.getUsername());
        if (user == null || !user.isActive()) throw new EntityNotFoundException("User not found");
        return ResponseEntity.ok(toResponse(user));
    }

    /**
     *
     * @param userDetails
     * @param request
     * @param picture
     * @return
     * @throws IOException
     */
    @PatchMapping
    @Operation(summary = "Update profile", description = "Update profile of currently logged-in user. Must be logged in.")
    public ResponseEntity<UserDto.UserResponse> updateOwnProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart(required = false) UserDto.UpdateProfileRequest request,
            @RequestPart(required = false) MultipartFile picture
    ) throws IOException {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userService.findByEmail(userDetails.getUsername());
        if (user == null || !user.isActive()) throw new EntityNotFoundException("User not found");

        if (request != null && request.username() != null) user.setName(request.username());

        if (request != null && request.gender() != null) {
            user.setGender(request.gender());
        }

        if (picture != null) {
            String fileType = picture.getContentType();
            if (fileType != null && fileType.startsWith("image")) {
                user = userService.uploadImage(user, picture);
            } else {
                throw new EntityNotFoundException("File type incorrect");
            }
        }
        return ResponseEntity.ok(toResponse(user));
    }

    /**
     * Soft deletes the authenticated user's account by marking as inactive and anonymizing data.
     * @return void
     */
    @DeleteMapping
    @Operation(summary = "Deactivate account", description = "Marks currently logged-in user as inactive and anonymizes data")
    public ResponseEntity<Void> deleteAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email);

        if (user == null || !user.isActive()) return ResponseEntity.notFound().build();

        userService.deleteUser(user.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     *
     * @param userDetails
     * @param request
     * @return
     */
    @PatchMapping("/password")
    @Operation(summary = "Update password", description = "Update password of currently logged-in user. Must be logged in.")
    public ResponseEntity<UserDto.UserResponse> updatePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserDto.UpdatePassword request
    ) {
        if (userDetails == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        User user = userService.findByEmail(userDetails.getUsername());
        if (user == null || !user.isActive()) throw new EntityNotFoundException("User not found");

        User updatedUser = userService.updatePassword(user, request);
        return ResponseEntity.ok(toResponse(updatedUser));
    }

    /**
     * Get profile picture of currently logged-in user
     * @param userDetails
     * @return
     */
    @GetMapping("/avatar")
    @Operation(summary = "Get profile picture")
    public ResponseEntity<byte[]> getAvatar(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        if (user == null || user.getImage() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header("Content-Type", user.getImageType())
                .body(user.getImage());
    }

    /**
     * Sets the logged-in user's image
     * @param userDetails the logged inn user's details
     * @param file the image file
     * @return a {@link ResponseEntity} containing the user's image as a byte array and
     * the image type
     * @throws IOException if an error occurs while reading or storing the file
     */
    @PutMapping("/avatar")
    @Operation(summary = "Upload profile picture")
    public ResponseEntity<UserDto.UserResponse> uploadAvatar(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (userDetails == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        User user = userService.findByEmail(userDetails.getUsername());
        if (user == null || !user.isActive()) throw new EntityNotFoundException("User not found");

        User updatedUser = userService.uploadImage(user, file);
        return ResponseEntity.ok(toResponse(updatedUser));
    }

    /**
     * Deletes the logged-in user's image
     *
     * @param userDetails the logged inn user's details
     * @return a {@link ResponseEntity} containing the user's details
     *
     */
    @DeleteMapping("/avatar")
    @Operation(summary = "Delete profile picture")
    public ResponseEntity<Void> deleteAvatar(@AuthenticationPrincipal CustomUserDetails userDetails){
        if (userDetails == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        User user = userService.findByEmail(userDetails.getUsername());
        if (user == null || !user.isActive()) throw new EntityNotFoundException("User not found");

        userService.deleteImage(user);
        return ResponseEntity.noContent().build();
    }

    /**
     * Maps a {@link User} entity to a {@link UserDto.UserResponse} DTO.
     * @param user the {@link User} entity to map
     * @return a {@link UserDto.UserResponse} containing mapped data
     */
    private UserDto.UserResponse toResponse(User user) {
        return new UserDto.UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getGender(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt(),
                user.getImage(),
                user.getImageType()
        );
    }
}
