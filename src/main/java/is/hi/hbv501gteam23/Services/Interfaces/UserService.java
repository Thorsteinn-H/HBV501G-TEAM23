package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user to find
     * @return the matching {@link User} entity
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their id.
     *
     * @param id the id address of the user to find
     * @return the matching {@link User} entity
     */
    User findById(Long id);

    /**
     * Creates a new user in the system.
     *
     * @param request  the {@link UserDto.CreateUserRequest} containing user details
     * @return the created {@link User} entity
     */
    User createUser(UserDto.CreateUserRequest request);

    /**
     * Updates an existing user.
     *
     * @param id       the ID of the user to update
     * @param request  the {@link UserDto.PatchUserRequest} containing fields to update
     * @return the updated {@link User} entity
     */
    User updateUser(Long id, UserDto.PatchUserRequest request);

    /**
     * Performs a soft delete a user by marking them as inactive
     * instead of removing them permanently.
     *
     * @param id  the id of the user to delete
     */
    void deleteUser(Long id);

    /**
     *
     * @param id
     */
    void deactivateUser(Long id);

    /**
     * Uploads an image file to a specific user.
     *
     * @param user      the user uploading the specific image
     * @param file      the image file to upload
     * @return the updated {@link User} entity with the new image data
     * @throws IOException if an error occurs while reading or storing the file
     */
    User uploadImage(User user, MultipartFile file) throws IOException;

    /**
     * Deletes the image associated with a user.
     *
     * @param user the user whose image should be deleted
     * @return the updated {@link User} entity with the image removed
     */
    User deleteImage(User user);

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

    /**
     * Finds users using optional filters and sorting.
     *
     * @param filter filter and sort parameters
     * @return list of {@link User} entities matching the filters
     */
    List<User> listUsers(UserDto.UserFilter filter);
}
