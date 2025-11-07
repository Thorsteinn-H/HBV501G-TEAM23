package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;

import java.util.List;

public interface AuthService {
    User login(String email, String password);
    User registerUser(UserDto.CreateUserRequest request);
    List<User> getAllActiveUsers();
    User findByEmail(String email);
    User findById(Long id);
    boolean validatePassword(String rawPassword, String hashedPassword);
    void softDeleteUser(Long id);
}
