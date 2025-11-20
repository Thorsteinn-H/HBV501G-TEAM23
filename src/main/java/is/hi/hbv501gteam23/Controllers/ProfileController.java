package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.dto.ProfileDto;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Security.CustomUserDetails;
import is.hi.hbv501gteam23.Services.Interfaces.ProfileService;
import is.hi.hbv501gteam23.Services.Interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * REST controller for managing the currently authenticated user's profile.
 */
@RestController
@RequestMapping("/profile")
@Tag(name = "Profile", description = "Operations for authenticated users only")
@Validated
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;
    private final ProfileService profileService;

    /**
     * Get profile of currently logged-in user
     *
     * @param userDetails the logged-in user's profile information
     * @return a {@link UserDto.UserResponse} containing the mapped data
     */
    @GetMapping
    @Operation(summary = "Get current user profile", description = "Get profile of currently logged-in user. Must be logged in.")
    public ResponseEntity<ProfileDto.ProfileResponse> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        return ResponseEntity.ok(toResponse(user));
    }

    /**
     * Updates the profile of the currently logged-in user.
     *
     * @param userDetails the details of the authenticated user
     * @param request optional request object with updated profile data
     * @return the updated user profile response
     * @throws IllegalArgumentException if the uploaded file is invalid
     */
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update profile", description = "Updates profile information for logged-in user")
    public ResponseEntity<ProfileDto.ProfileResponse> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserDto.UpdateProfileRequest request
    ) {
        User user = getAuthenticatedUser(userDetails);

        user = profileService.updateProfile(
                user,
                new ProfileDto.UpdateProfileRequest(request.username(), request.gender())
        );

        return ResponseEntity.ok(toResponse(user));
    }

    /**
     * Updates the authenticated user's password.
     *
     * @param userDetails the authenticated user's details injected by Spring Security
     * @param request     the password update request containing the current and new password
     * @return updated ProfileResponse
     * @throws RuntimeException if old password does not match
     */
    @PatchMapping("/password")
    @Operation(summary = "Change password", description = "Update the logged-in user's password.")
    public ResponseEntity<ProfileDto.ProfileResponse> changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserDto.UpdatePassword request
    ) {
        User user = getAuthenticatedUser(userDetails);
        User updatedUser = userService.updatePassword(user, new UserDto.UpdatePassword(request.newPassword(), request.oldPassword()));
        return ResponseEntity.ok(toResponse(updatedUser));
    }

    /**
     * Soft deletes the authenticated user's account by marking as inactive and anonymizing data.
     *
     * @param userDetails Authenticated user details
     * @return 204 No Content
     */
    @DeleteMapping
    @Operation(summary = "Deactivate account", description = "Soft-deletes the logged-in user by marking as inactive")
    public ResponseEntity<Void> deactivateAccount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        profileService.deactivateAccount(user);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves the authenticated user's profile picture.
     *
     * @param userDetails the authenticated user's details injected by Spring Security
     * @return {@link ResponseEntity} with status 200 (OK) containing the image bytes in the body
     * and a {@code Content-Type} header matching the stored image type,
     * or 404 (Not Found) if no profile image exists for the user
     */
    @GetMapping("/avatar")
    @Operation(summary = "Get profile picture", description = "Retrieves the logged-in user's profile picture")
    public ResponseEntity<byte[]> getAvatar(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        if (user.getProfileImage() == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No profile image found");

        byte[] imageBytes = user.getProfileImage().getImageData();
        String contentType = user.getProfileImage().getImageType();

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePrivate())
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"avatar_" + user.getId() + "\"")
            .body(imageBytes);
    }

    /**
     * Uploads or updates the authenticated user's profile picture.
     *
     * @param userDetails the authenticated user's details
     * @param file a multipart image file
     * @return updated ProfileResponse
     * @throws IOException if file cannot be read
     */
    @PutMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload profile picture", description = "Uploads or updates the logged-in user's profile picture")
    public ResponseEntity<ProfileDto.ProfileResponse> uploadAvatar(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        User user = getAuthenticatedUser(userDetails);
        User updatedUser = userService.uploadImage(user, file);
        return ResponseEntity.ok(toResponse(updatedUser));
    }

    /**
     * Deletes the authenticated user's profile picture.
     *
     * @param userDetails the authenticated user's details
     * @return 204 No Content
     */
    @DeleteMapping("/avatar")
    @Operation(summary = "Delete profile picture", description = "Removes logged-in user's profile picture")
    public ResponseEntity<Void> deleteAvatar(@AuthenticationPrincipal CustomUserDetails userDetails){
        User user = getAuthenticatedUser(userDetails);
        userService.deleteImage(user);
        return ResponseEntity.noContent().build();
    }

    private User getAuthenticatedUser(CustomUserDetails userDetails) {
        if (userDetails == null) throw new EntityNotFoundException("User not authenticated");
        return userService.findByEmail(userDetails.getUsername())
            .filter(User::isActive)
            .orElseThrow(() -> new EntityNotFoundException("User not found or inactive"));
    }

    /**
     * Maps a {@link User} entity to a {@link UserDto.UserResponse} DTO.
     * @param user the {@link User} entity to map
     * @return a {@link UserDto.UserResponse} containing mapped data
     */
    private ProfileDto.ProfileResponse toResponse(User user) {
        String imageUrl = (user.getProfileImage() != null) ? "/profile/avatar" : null;
        return new ProfileDto.ProfileResponse(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getGender(),
            user.getCreatedAt(),
            imageUrl
        );
    }
}
