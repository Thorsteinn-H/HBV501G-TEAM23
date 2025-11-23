package is.hi.hbv501gteam23.Persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import is.hi.hbv501gteam23.Persistence.enums.Gender;
import is.hi.hbv501gteam23.Persistence.enums.SystemRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

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
        SystemRole role,

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
        SystemRole role,

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
     * Filter parameters for listing users.
     * All fields are optional; when {@code null} or blank, the filter is ignored.
     *
     * @param email    email filter (contains, case-insensitive)
     * @param username username filter (contains, case-insensitive)
     * @param role     system role filter
     * @param active   active status filter
     * @param sortBy   sort field (e.g. "id", "email", "username", "createdAt")
     * @param sortDir  sort direction, either {@code "ASC"} or {@code "DESC"}
     */
    public record UserFilter(
        String email,
        String username,
        SystemRole role,
        Boolean active,
        String sortBy,
        String sortDir
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
        String email,
        String username,
        Gender gender,
        SystemRole role,
        Boolean isActive,
        LocalDateTime createdAt,
        String profileImageUrl
    ) {}
}
