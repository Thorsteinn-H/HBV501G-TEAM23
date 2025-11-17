package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Image;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.Repositories.AuthRepository;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Services.Interfaces.FavoriteService;
import is.hi.hbv501gteam23.Services.Interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service implementation for handling user management
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final AuthRepository authRepository;
    private final FavoriteService favoriteService;
    private final PasswordEncoder passwordEncoder;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final List<String> ALLOWED_TYPES = List.of(
        "image/jpeg",
        "image/png",
        "image/gif"
    );

    /**
     * Retrieves all users.
     *
     * @return a list of {@link User} entities
     */
    @Override
    public List<User> getAllUsers() {
        return authRepository.findAllUsers();
    }

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user to find
     * @return the matching {@link User} entity
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return authRepository.findByEmail(email);
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
     * @param rawPassword   the plain text password provided for verification
     * @param storedPassword  the stored hashed password
     * @return {@code true} if the raw password matches the hashed password otherwise {@code false}
     */
    @Override
    public boolean validatePassword(String rawPassword, String storedPassword) {
        return passwordEncoder.matches(rawPassword, storedPassword);
    }

    /**
     *
     * @param request  the {@link UserDto.CreateUserRequest} containing user details
     * @return
     */
    @Override
    public User createUser(UserDto.CreateUserRequest request) {
        if (authRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setName(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        if (request.gender() != null) {
            user.setGender(request.gender());
        }
        user.setRole(request.role() != null ? request.role() : "USER");
        user.setActive(request.isActive() != null ? request.isActive() : true);
        user.setCreatedAt(LocalDateTime.now());

        return authRepository.save(user);
    }

    /**
     *
     * @param id       the ID of the user to update
     * @param request  the {@link UserDto.PatchUserRequest} containing fields to update
     * @return
     */
    @Override
    public User updateUser(Long id, UserDto.PatchUserRequest request) {
        User user = findById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (request.email() != null) user.setEmail(request.email());
        if (request.username() != null) user.setName(request.username());
        if (request.password() != null) user.setPasswordHash(passwordEncoder.encode(request.password()));

        if (request.gender() != null) {
            user.setGender(request.gender());
        }
        if (request.role() != null) user.setRole(request.role());
        if (request.isActive() != null) user.setActive(request.isActive());

        return authRepository.save(user);
    }

    /**
     * Performs a soft delete a user by marking them as inactive
     * instead of removing them permanently.
     *
     * @param id  the id of the user to delete
     */
    @Override
    public void deleteUser(Long id) {
        User user = findById(id);
        if (user != null && user.isActive()) {
            user.setActive(false);
            user.setDeletedAt(LocalDateTime.now());
            user.setEmail("deleted_" + id + "@example.com");
            user.setName("Deleted User " + id);
            user.setPasswordHash(UUID.randomUUID().toString());
            user.setGender(null);
            authRepository.save(user);
        }
    }

    /**
     * Uploads an image file to a specific user.
     * @param user
     * @param file
     * @return
     * @throws IOException
     */
    @Override
    public User uploadImage(User user, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("File is required");
        if (!ALLOWED_TYPES.contains(file.getContentType()))
            throw new IllegalArgumentException("Unsupported file type: " + file.getContentType());
        if (file.getSize() > MAX_FILE_SIZE)
            throw new IllegalArgumentException("File size exceeds 5MB");

        Image image = user.getProfileImage();

        image.setImageData(file.getBytes());
        image.setImageType(file.getContentType());

        user.setProfileImage(image);
        return authRepository.save(user);
    }

    /**
     * Deletes the image associated with a user.
     *
     * @param user  the user whose image should be deleted
     * @return the updated {@link User} object with image cleared
     */
    @Override
    public User deleteImage(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");

        user.setProfileImage(null);
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
        return user.getProfileImage().getImageData();
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
        return user.getProfileImage().getImageType();
    }

    /**
     * Updates the password for the specified user.
     *
     * @param user     the user whose password will be updated
     * @param request  a {@link UserDto.UpdatePassword} containing the old and new passwords
     * @return the updated {@link User} object after the password change
     */
    @Override
    public User updatePassword(User user, UserDto.UpdatePassword request) {
        if (!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Old password does not match" );
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        return authRepository.save(user);
    }

    /**
     * Updates the username of the specified user.
     *
     * @param user     the user whose username will be updated
     * @param request  a {@link UserDto.UpdateProfileRequest}  containing the new username
     * @return the updated {@link User} object after the username change
     */
    @Override
    public User updateProfile(User user, UserDto.UpdateProfileRequest request) {
        if (request.username() != null) user.setName(request.username());
        if (request.gender() != null) {
            user.setGender(request.gender());
        }
        return authRepository.save(user);
    }
}
