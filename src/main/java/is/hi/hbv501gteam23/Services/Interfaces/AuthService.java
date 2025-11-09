package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AuthService {

    /**
     * Logs inn a user with the given email and password.
     *
     * @param email     the email address of the user
     * @param password  the plain text password provided by the user
     * @return the {@link User} entity if credentials are valid,
     *
     */
    User login(String email, String password);


    /**
     * Registers a new user with the provided details.
     *
     * @param request  a {@link UserDto.CreateUserRequest} containing the users information
     * @return the newly created {@link User} entity
     */
    User registerUser(UserDto.CreateUserRequest request);

    /**
     * Retrieves all users who are currently active.
     *
     * @return a list of active {@link User} entities
     */
    List<User> getAllActiveUsers();

    /**
     * Finds a user by their email address.
     *
     * @param email  the email address of the user to find
     * @return the matching {@link User} entity
     */
    User findByEmail(String email);

    /**
     * Finds a user by their id.
     *
     * @param id the id address of the user to find
     * @return the matching {@link User} entity
     */
    User findById(Long id);

    /**
     * Validates a raw password against a hashed password.
     *
     * @param rawPassword   the plain text password provided for verification
     * @param hashedPassword  the stored hashed password
     * @return {@code true} if the raw password matches the hashed password otherwise {@code false}
     */
    boolean validatePassword(String rawPassword, String hashedPassword);

    /**
     * Performs a soft delete a user by marking them as inactive
     * instead of removing them permanently.
     *
     * @param id  the id of the user to delete
     */
    void softDeleteUser(Long id);

    /**
     * Ensures that a favorites entry exists for the given user
     * @param userId The ID of the user
     */
    void ensureFavoritesExists(Long userId);

    /**
     * Uploads an image file to a specific user.
     *
     * @param user      the user uploading the specific image
     * @param file      the image file to upload
     * @param fileType  the type of image file
     * @return the updated {@link User} entity with the new image data
     * @throws IOException if an error occurs while reading or storing the file
     */
    User uploadImage(User user, MultipartFile file, String fileType) throws IOException;

    /**
     * Updates the password for the specified user.
     *
     * @param user     the user whose password will be updated
     * @param request  a {@link UserDto.updatePassword} object containing the old and new passwords
     * @return the updated {@link User} entity after the password change
     */
    User updatePassword(User user, UserDto.updatePassword request);

    /**
     * Updates the gender of the specified user.
     *
     * @param user     the user whose gender will be updated
     * @param request  a {@link UserDto.updateGender} object containing the new gender information
     * @return the updated {@link User} entity after the gender change
     */
    User updateGender(User user,  UserDto.updateGender request);

    /**
     * Updates the username of the specified user.
     *
     * @param user     the user whose username will be updated
     * @param request  a {@link UserDto.updateUsername} object containing the new username
     * @return the updated {@link User} entity after the username change
     */
    User updateUsername(User user, UserDto.updateUsername request);

    /**
     * Retrieves the stored image data for the specified user.
     *
     * @param user  the user whose image will be retrieved
     * @return a byte array containing the image data
     */
    byte[] getImage(User user);

    /**
     * Retrieves the type of image file for a users image
     *
     * @param user  the user whose image type will be retrieved
     * @return a {@link String} representing the image type (e.g., "image/png", "jpg"),
     *
     */
    String getImageType(User user);
}
