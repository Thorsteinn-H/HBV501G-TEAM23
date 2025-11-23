package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.Repositories.AuthRepository;
import is.hi.hbv501gteam23.Persistence.dto.ProfileDto;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Services.Interfaces.ProfileService;
import is.hi.hbv501gteam23.Services.Interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImplementation implements ProfileService {
    private final AuthRepository authRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

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
     * Updates the profile information of the specified user.
     * <p>
     * Currently supports updating username and gender.
     *
     * @param user    the user whose profile will be updated
     * @param request a {@link ProfileDto.UpdateProfileRequest} containing the new profile values
     * @return the updated {@link User} object after the profile change
     */
    @Override
    public User updateProfile(User user, ProfileDto.UpdateProfileRequest request) {
        if (request.username() != null) user.setName(request.username());
        if (request.gender() != null) {
            user.setGender(request.gender());
        }
        return authRepository.save(user);
    }

    /**
     * Updates the password for the specified user.
     * <p>
     * Verifies that the old password matches before setting the new password.
     *
     * @param user    the user whose password will be updated
     * @param request a {@link ProfileDto.UpdatePassword} containing the old and new passwords
     * @return the updated {@link User} object after the password change
     * @throws RuntimeException if the old password does not match
     */
    @Override
    public User updatePassword(User user, ProfileDto.UpdatePassword request) {
        if (!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Old password does not match" );
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        return authRepository.save(user);
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
