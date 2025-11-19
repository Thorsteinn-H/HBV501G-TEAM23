package is.hi.hbv501gteam23.Persistence.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import is.hi.hbv501gteam23.Persistence.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDto {

    /**
     * Request body for login
     * @param email user's email
     * @param password user's password
     */
    public record LoginUserRequest(
            @Schema(example = "admin@team23.com")
            @Email(message = "Invalid email")
            @NotBlank(message = "Email is required")
            String email,

            @Schema(example = "password")
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
            @Schema(example = "user2@example.com")
            @Email(message = "Invalid email")
            @NotBlank(message = "Email is required")
            String email,

            @Schema(example = "user2")
            @NotBlank(message = "Username is required")
            String username,

            @Schema(example = "password")
            @NotBlank(message = "Password is required")
            String password,

            @Schema(example = "FEMALE")
            Gender gender
    ) {}

    /**
     * Response body for login
     * @param accessToken JWT access token
     * @param refreshToken JWT access token
     * @param user logged-in user info
     */
    public record AuthResponse(
            String accessToken,
            String refreshToken,
            UserDto.UserResponse user
    ) {}
}
