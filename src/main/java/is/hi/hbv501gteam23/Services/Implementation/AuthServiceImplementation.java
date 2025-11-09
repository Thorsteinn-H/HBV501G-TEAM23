package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.Repositories.AuthRepository;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import is.hi.hbv501gteam23.Services.Interfaces.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for handling user authentication and registration
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {
    private final AuthRepository authRepository;
    private final FavoriteService favoriteService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Logs inn a user with the given email and password.
     *
     * @param email     the email address of the user
     * @param password  the plain text password provided by the user
     * @return the {@link User} entity if credentials are valid,
     *
     */
    @Override
    public User login(String email, String password) {
        User user = findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    /**
     * Registers a new user with the provided details.
     *
     * @param request  a {@link UserDto.CreateUserRequest} containing the users information
     * @return the newly created {@link User} entity
     */
    @Override
    public User registerUser(UserDto.CreateUserRequest request) {
        if (findByEmail(request.email()) != null) {
            throw new RuntimeException("Email already in use");
        }
        User user = new User();
        user.setEmail(request.email());
        user.setName(request.userName());
        user.setRole("USER");
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);
        user.setGender(request.gender());

        User saved = authRepository.save(user);
        favoriteService.getOrCreateFavorites(saved.getId());
        return saved;
    }

    /**
     * Retrieves all users who are currently active.
     *
     * @return a list of active {@link User} entities
     */
    @Override
    public List<User> getAllActiveUsers() {
        return authRepository.findAllActiveUsers();
    }

    /**
     * Finds a user by their email address.
     *
     * @param email  the email address of the user to find
     * @return the matching {@link User} entity
     */
    @Override
    public User findByEmail(String email) {
        return authRepository.findByEmail(email).orElse(null);
    }

    /**
     * Finds a user by their id.
     *
     * @param id the id of the user to find
     * @return the matching {@link User} entity
     */
    @Override
    public User findById(Long id) {
        return authRepository.findById(id).orElse(null);
    }

    /**
     * Validates a raw password against a hashed password.
     *
     * @param inputPassword   the plain text password provided for verification
     * @param storedPassword  the stored hashed password
     * @return {@code true} if the raw password matches the hashed password otherwise {@code false}
     */
    @Override
    public boolean validatePassword(String inputPassword, String storedPassword) {
        return passwordEncoder.matches(inputPassword, storedPassword);
    }

    /**
     * Ensures a favorite exists for a specific user
     * @param userId The id of the user
     */
    @Override
    public void ensureFavoritesExists(Long userId) {
        favoriteService.getOrCreateFavorites(userId);
    }

    /**
     * Performs a soft delete a user by marking them as inactive
     * instead of removing them permanently.
     *
     * @param id  the id of the user to delete
     */
    @Override
    public void softDeleteUser(Long id) {
        User user = findById(id);
        if (user != null && user.isActive()) {
            user.setActive(false);
            user.setDeletedAt(LocalDateTime.now());
            user.setEmail("deleted_" + id + "@example.com");
            user.setName("Deleted User " + id);
            user.setPasswordHash(null);
            user.setGender(null);
            authRepository.save(user);
        }
    }

    /**
     * Uploads an image file to a specific user.
     *
     * @param user      the user uploading the specific image
     * @param file      the image file to upload
     * @param filetype  the type of image file
     * @return the updated {@link User} object with the new image data
     * @throws IOException if an error occurs while reading or storing the file
     */
    @Override
    public User uploadImage(User user, MultipartFile file, String filetype) throws IOException {

        user.setImageType(filetype);
        user.setImage(file.getBytes());
        return authRepository.save(user);
    }

    /**
     * Retrieves the stored image data for the specified user.
     *
     * @param user  the user whose image will be retrieved
     * @return a byte array containing the image data
     */
    @Override
    public byte[] getImage(User user) {
        return user.getImage();
    }

    /**
     * Retrieves the type of image file for a users image
     *
     * @param user  the user whose image type will be retrieved
     * @return a {@link String} representing the image type (e.g., "image/png", "jpg"),
     *
     */
    @Override
    public String getImageType(User user) {
        return user.getImageType();
    }

    /**
     * Updates the password for the specified user.
     *
     * @param user     the user whose password will be updated
     * @param request  a {@link UserDto.updatePassword} containing the old and new passwords
     * @return the updated {@link User} object after the password change
     */
    @Override
    public User updatePassword(User user, UserDto.updatePassword request) {

        if (!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Old password does not match" );

        } else {

            String hashedNewPassword = passwordEncoder.encode(request.newPassword());
            user.setPasswordHash(hashedNewPassword);
            return authRepository.save(user);
        }

    }


    /**
     * Updates the gender of the specified user.
     *
     * @param user     the user whose gender will be updated
     * @param request  a {@link UserDto.updateGender} containing the new gender information
     * @return the updated {@link User} object after the gender change
     */
    @Override
    public User updateGender(User user,  UserDto.updateGender request) {

        user.setGender(request.newGender());
        return authRepository.save(user);

    }

    /**
     * Updates the username of the specified user.
     *
     * @param user     the user whose username will be updated
     * @param request  a {@link UserDto.updateUsername}  containing the new username
     * @return the updated {@link User} object after the username change
     */
    @Override
    public User updateUsername(User user, UserDto.updateUsername request) {

        user.setName(request.newUsername());
        return authRepository.save(user);
    }

}
