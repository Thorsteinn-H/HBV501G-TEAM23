package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.dto.ProfileDto;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Services.Interfaces.ProfileService;
import is.hi.hbv501gteam23.Services.Interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImplementation implements ProfileService {

    private final UserService userService;

    /**
     * Retrieves an active user profile by email.
     *
     * @param email the email of the user whose profile is being fetched
     * @return the matching active {@link User}
     * @throws EntityNotFoundException if no active user with the given email exists
     */
    @Override
    public User getProfile(String email) {
        return userService.findByEmail(email)
            .filter(User::isActive)
            .orElseThrow(() -> new EntityNotFoundException("Active user not found for email: " + email));
    }

    /**
     * Updates profile information (username or gender) for a given user.
     *
     * @param user    the user whose profile should be updated
     * @param request profile update request containing new profile values
     * @return the updated {@link User}
     * @throws IllegalArgumentException if the request is null
     */
    @Override
    public User updateProfile(User user, ProfileDto.UpdateProfileRequest request) {
        if (request == null) throw new IllegalArgumentException("UpdateProfileRequest cannot be null");
        UserDto.UpdateProfileRequest userRequest = new UserDto.UpdateProfileRequest(
            request.username(),
            request.gender()
        );
        return userService.updateProfile(user, userRequest);
    }

    /**
     * Updates the password for a given user.
     *
     * @param user    the user whose password should be changed
     * @param request an object containing the old and new passwords
     * @return the updated {@link User} after password change
     * @throws IllegalArgumentException if the request is null
     */
    @Override
    public User updatePassword(User user, ProfileDto.UpdatePassword request) {
        if (request == null) throw new IllegalArgumentException("UpdatePassword request cannot be null");
        UserDto.UpdatePassword userRequest = new UserDto.UpdatePassword(
            request.newPassword(),
            request.oldPassword()
        );
        return userService.updatePassword(user, userRequest);
    }

    /**
     * Uploads or replaces the user's avatar image.
     *
     * @param user the user whose avatar is being updated
     * @param file the uploaded image file
     * @return the updated {@link User} with a new or updated avatar
     * @throws IllegalArgumentException if the user is null
     * @throws IOException if an error occurs while processing the file
     */
    @Override
    public User uploadAvatar(User user, MultipartFile file) throws IOException {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        return userService.uploadImage(user, file);
    }

    /**
     * Permanently removes the user's avatar image.
     *
     * @param user the user whose avatar should be deleted
     * @return the updated {@link User} with no profile image
     * @throws IllegalArgumentException if the user is null
     */
    @Override
    public User deleteAvatar(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        return userService.deleteImage(user);
    }

    /**
     * Performs a soft delete by deactivating the user account.
     *
     * @param user the user account to deactivate
     */
    @Override
    public void deactivateAccount(User user) {
        userService.deactivateUser(user.getId());
    }
}
