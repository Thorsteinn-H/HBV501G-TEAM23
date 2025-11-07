package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AuthService {
    User login(String email, String password);
    User registerUser(UserDto.CreateUserRequest request);
    List<User> getAllActiveUsers();
    User findByEmail(String email);
    User findById(Long id);
    boolean validatePassword(String rawPassword, String hashedPassword);
    void softDeleteUser(Long id);

    /**
     * Ensures that a favorites entry exists for the given user
     * @param userId The ID of the user
     */
    void ensureFavoritesExists(Long userId);

    User uploadImage(User user, MultipartFile file, String fileType) throws IOException;
    User updatePassword(User user, UserDto.updatePassword request);
    User updateGender(User user,  UserDto.updateGender request);
    User updateUsername(User user, UserDto.updateUsername request);
    byte[] getImage(User user);
    String getImageType(User user);
}
