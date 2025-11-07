package is.hi.hbv501gteam23.Persistence.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDto {

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
     * Response body for login
     * @param token JWT token
     * @param user logged-in user info
     */
    public record LoginResponse(
            String token,
            UserDto.UserResponse user
    ) {}
}
