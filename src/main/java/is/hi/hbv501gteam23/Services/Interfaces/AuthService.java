package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.dto.AuthDto;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import jakarta.validation.Valid;

public interface AuthService {
    /**
     * Logs in a user with the given email and password.
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
    User register(AuthDto.@Valid RegisterUserRequest request);
}
