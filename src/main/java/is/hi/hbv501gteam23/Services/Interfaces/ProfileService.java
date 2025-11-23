package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.dto.ProfileDto;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface ProfileService {

    /**
     * Retrieves an active user profile by its email address.
     *
     * @param email the email of the user whose profile is requested
     * @return the matching active {@link User}
     */
    User getProfile(String email);

    /**
     * Updates the profile information (e.g. username, gender) for a given user.
     *
     * @param user    the user whose profile should be updated
     * @param request profile update request containing new values
     * @return the updated {@link User}
     */
    User updateProfile(User user, ProfileDto.UpdateProfileRequest request);

    /**
     * Updates the password for the given user.
     *
     * @param user    the user whose password will be changed
     * @param request DTO containing the old and new password
     * @return the updated {@link User} after password change
     */
    User updatePassword(User user, ProfileDto.UpdatePassword request);

    /**
     * Uploads or replaces the avatar image for the given user.
     *
     * @param user the user whose avatar is being updated
     * @param file the image file to upload
     * @return the updated {@link User} with the new avatar
     * @throws IOException if an error occurs while reading the image file
     */
    User uploadAvatar(User user, MultipartFile file) throws IOException;

    /**
     * Deletes the avatar image associated with the given user.
     *
     * @param user the user whose avatar should be removed
     * @return the updated {@link User} without an avatar
     */
    User deleteAvatar(User user);

    /**
     * Deactivates (soft-deletes) the given user account.
     *
     * @param user the user to deactivate
     */
    void deactivateAccount(User user);
}
