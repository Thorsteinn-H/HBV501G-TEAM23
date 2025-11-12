package is.hi.hbv501gteam23.Persistence.dto;

import is.hi.hbv501gteam23.Persistence.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDto {

    /**
     * Request body for login
     * @param email user's email
     * @param password user's password
     */
    public record LoginRequest(
            @Email(message = "Invalid email")
            @NotBlank(message = "Email is required")
            String email,

            @NotBlank(message = "Password is required")
            String password
    ) {}

    /**
     * Request body for registering a new user.
     * @param email user's email
     * @param username user's chosen username
     * @param password user's password
     * @param gender user's gender
     */
    public record RegisterUserRequest(
            String email,
            String username,
            String password,
            Gender gender // optional
    ) {}

    /**
     * Response body for login
     * @param token JWT token
     * @param user logged-in user info
     */
    public record LoginResponse(
            String token,
            UserDto.UserResponse user
    ) {}
}
