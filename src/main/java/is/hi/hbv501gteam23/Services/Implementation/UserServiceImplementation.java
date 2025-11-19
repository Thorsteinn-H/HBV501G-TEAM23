package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Image;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.Repositories.AuthRepository;
import is.hi.hbv501gteam23.Persistence.Specifications.UserSpecifications;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Security.PasswordValidationUtil;
import is.hi.hbv501gteam23.Services.Interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
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
    private final PasswordEncoder passwordEncoder;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final List<String> ALLOWED_TYPES = List.of(
        "image/jpeg",
        "image/png",
        "image/gif"
    );

    public List<User> findUsers(String email, String name, String role, Boolean active, String sortBy, String order) {
        Specification<User> spec = UserSpecifications.emailContains(email)
            .and(UserSpecifications.nameContains(name))
            .and(UserSpecifications.roleEquals(role))
            .and(UserSpecifications.isActive(active));
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        if (sortBy != null) {
            Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
            sort = switch (sortBy.toLowerCase()) {
                case "email" -> Sort.by(direction, "email");
                case "name" -> Sort.by(direction, "name");
                case "createdat" -> Sort.by(direction, "createdAt");
                default -> Sort.by(direction, "id");
            };
        }
        return authRepository.findAll(spec, sort);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user to find
     * @return an {@link Optional} containing the matching {@link User} if found,
     * or empty if not found
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return authRepository.findByEmail(email);
    }

    /**
     * Finds a user by their id.
     *
     * @param id the id of the user to find
     * @return the matching {@link User} entity, or {@code null} if not found
     */
    @Override
    public User findById(Long id) {
        return authRepository.findById(id).orElse(null);
    }

    /**
     * Creates a new user.
     * Validates that the email is not already in use. Sets default role to {@code "USER"}
     * and default active status to {@code true} if not provided.
     *
     * @param request the {@link UserDto.CreateUserRequest} containing user details
     * @return the created {@link User} entity
     * @throws RuntimeException if the email is already in use
     */
    @Override
    public User createUser(UserDto.CreateUserRequest request) {
        if (authRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Incorrect email or password");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setName(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        if (request.gender() != null) {
            user.setGender(request.gender());
        }
        user.setRole(request.role());
        user.setActive(request.isActive() != null ? request.isActive() : true);
        user.setCreatedAt(LocalDateTime.now());

        return authRepository.save(user);
    }

    /**
     * Updates an existing user by ID.
     *
     * @param id      the ID of the user to update
     * @param request the {@link UserDto.PatchUserRequest} containing fields to update
     * @return the updated {@link User} entity
     * @throws RuntimeException if the user is not found
     */
    @Override
    public User updateUser(Long id, UserDto.PatchUserRequest request) {
        User user = findById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (request.password() != null && !PasswordValidationUtil.isValid(request.password())) {
            throw new IllegalArgumentException("Password does not meet complexity requirements");
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
    public void deactivateUser(Long id) {
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
     * Deletes a user from the database.
     *
     * @param id  the id of the user to delete
     */
    @Override
    public void deleteUser(Long id) {
        User user = findById(id);
        if (user != null) {
            authRepository.delete(user);
        }
    }

     * Uploads a profile image for a specific user.
     * <p>
     * Validates that the file is present, has an allowed content type and does not exceed
     * the maximum file size. The image data and type are stored on the {@link User}'s
     * {@link Image} entity.
     *
     * @param user the user to associate the image with
     * @param file the uploaded image file
     * @return the updated {@link User} with the new image
     * @throws IOException              if an I/O error occurs while reading the file
     * @throws IllegalArgumentException if the file is missing, unsupported or too large
     */
    @Override
    public User uploadImage(User user, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("File is required");
        if (file.getSize() > MAX_FILE_SIZE) throw new IllegalArgumentException("File size exceeds 5MB");

        String type = file.getContentType();
        if (!ALLOWED_TYPES.contains(type))
            throw new IllegalArgumentException("Unsupported file type: " + file.getContentType());

        try (var is = file.getInputStream()) {
            if (ImageIO.read(is) == null)
                throw new IllegalArgumentException("Invalid image content");
        }

        Image image = user.getProfileImage();
        if (image == null) image = new Image();

        image.setImageData(file.getBytes());
        image.setImageType(type);
        user.setProfileImage(image);

        return authRepository.save(user);
    }

    /**
     * Deletes the image associated with a user.
     *
     * @param user the user whose image should be deleted
     * @return the updated {@link User} object with the image cleared
     * @throws IllegalArgumentException if the user is {@code null}
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
     * @param user the user whose image will be retrieved
     * @return a byte array containing the image data
     * @throws IllegalArgumentException if the user or profile image is {@code null}
     */
    @Override
    public byte[] getImage(User user) {
        return user.getProfileImage().getImageData();
    }

    /**
     * Retrieves the type of the image file for a user's profile image.
     *
     * @param user the user whose image type will be retrieved
     * @return a {@link String} representing the image MIME type
     *         (e.g., {@code "image/png"}, {@code "image/jpeg"})
     * @throws IllegalArgumentException if the user or profile image is {@code null}
     */
    @Override
    public String getImageType(User user) {
        return user.getProfileImage().getImageType();
    }

    /**
     * Updates the password for the specified user.
     * <p>
     * Verifies that the old password matches before setting the new password.
     *
     * @param user    the user whose password will be updated
     * @param request a {@link UserDto.UpdatePassword} containing the old and new passwords
     * @return the updated {@link User} object after the password change
     * @throws RuntimeException if the old password does not match
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
     * Updates the profile information of the specified user.
     * <p>
     * Currently supports updating username and gender.
     *
     * @param user    the user whose profile will be updated
     * @param request a {@link UserDto.UpdateProfileRequest} containing the new profile values
     * @return the updated {@link User} object after the profile change
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
