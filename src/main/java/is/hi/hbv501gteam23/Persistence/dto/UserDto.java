package is.hi.hbv501gteam23.Persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import is.hi.hbv501gteam23.Persistence.Entities.Image;
import is.hi.hbv501gteam23.Persistence.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDto {

    /**
     * Request body for creating a new user. Admin only.
     * @param email
     * @param username
     * @param password
     * @param gender
     * @param role
     * @param isActive
     */
    public record CreateUserRequest(
            @Schema(example = "admin@team23.com")
            @Email(message = "Invalid email")
            @NotBlank(message = "Email is required")
            String email,

            @Schema(example = "username")
            @NotBlank(message = "Username is required")
            String username,

            @Schema(example = "password")
            @NotBlank(message = "Password is required")
            String password,

            @Schema(example = "FEMALE")
            Gender gender,

            @Schema(example = "USER")
            String role,

            @Schema(example = "true")
            Boolean isActive
    ) {}

    /**
     * Request body for updating a user. Admin only.
     * All fields are optional, PATCH only updated non-null fields.
     * @param email
     * @param username
     * @param password
     * @param gender
     * @param role
     * @param isActive
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record PatchUserRequest(
            @Schema(example = "admin@team23.com")
            @Email(message = "Invalid email")
            String email,

            @Schema(example = "username")
            String username,

            @Schema(example = "password")
            String password,

            @Schema(example = "FEMALE")
            Gender gender,

            @Schema(example = "USER")
            String role,

            @Schema(example = "true")
            Boolean isActive
    ) {}

    /**
     * Request body for updating own profile info
     * All fields are optional. PATCH only updates non-null fields.
     * @param username
     * @param gender
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record UpdateProfileRequest(
            @Schema(example = "username")
            String username,
            @Schema(example = "MALE")
            Gender gender
    ) {}

    /**
     * Request body for updating own password
     * @param newPassword The new password for the user
     * @param oldPassword The old password for the user
     */
    public record UpdatePassword(
            @Schema(example = "newPassword456")
            String newPassword,
            @Schema(example = "oldPassword123")
            String oldPassword
    ) {}

    /**
     * Response body for retrieving a user
     * @param id the user's database ID
     * @param email user's email
     * @param username user's chosen username
     * @param gender user's gender
     * @param role user's role
     * @param isActive whether the user is active
     * @param createdAt timestamp when user was created
     */
    public record UserResponse(
            Long id,

            @Schema(example = "admin@team23.com")
            String email,

            @Schema(example = "username")
            String username,

            @Schema(example = "FEMALE")
            Gender gender,

            @Schema(example = "USER")
            String role,

            @Schema(example = "true")
            Boolean isActive,

            java.time.LocalDateTime createdAt,

            Image profileImage
    ) {}
}
