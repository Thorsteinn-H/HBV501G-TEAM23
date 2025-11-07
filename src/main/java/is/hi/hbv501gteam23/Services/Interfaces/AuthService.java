package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.User;

import java.util.List;

public interface AuthService {
    User login(String email, String password);
    User register(User user);
    List<User> getAllUsers();
    User findByEmail(String email);
    User findById(Long id);
    boolean validatePassword(String rawPassword, String hashedPassword);
    void deleteUser(Long id);
}
