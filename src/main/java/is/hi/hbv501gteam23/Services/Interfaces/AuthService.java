package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;

/**
 * Nafn : Þorsteinn H. Erlendsson
 * Tölvupóstur: the85@hi.is
 * Lýsing: Service interface for handling user authentication operations
 */
public interface AuthService {
    User login(String email, String password);
    User register(User user);
    User findByEmail(String email);
    User findById(Long id);
    boolean validatePassword(String rawPassword, String hashedPassword);

    //Kjaftæði
    User updatePassword(User user, UserDto.updatePassword request);
    User updateGender(User user,  UserDto.updateGender request);
    User updateUsername(User user, UserDto.updateUsername request);

}
