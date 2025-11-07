package is.hi.hbv501gteam23.Persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserDto {

    /**
     * Request body for creating a new user
     * @param email user's email
     * @param userName user's chosen username
     * @param password user's password
     * @param gender user's gender
     * @param role user's role (User, ADMIN)
     */
    public record CreateUserRequest(
            String email,
            String userName,
            String password,
            String gender, // optional
            String role
    ) {}

    /**
     * Request body for updating a user.
     * All fields are optional. PATCH only updates non-null fields.
     * @param email user's email
     * @param userName user's chosen username
     * @param password user's password
     * @param gender user's gender
     * @param role user's role (User, ADMIN)
     * @param isActive whether the user is active
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record PatchUserRequest(
            String email,
            String userName,
            String password,
            String gender,
            String role,
            Boolean isActive
    ) {}

    /**
     * Response body for retrieving a user
     * @param id the user's database ID
     * @param email user's email
     * @param userName user's chosen username
     * @param gender user's gender
     * @param role user's role
     * @param isActive whether the user is active
     * @param createdAt timestamp when user was created
     */
    public record UserResponse(
            Long id,
            String email,
            String userName,
            String gender,
            String role,
            Boolean isActive,
            java.time.LocalDateTime createdAt
    ){}

    public record updatePassword(
            String newPassword,
            String oldPassword
    ) {}

    public record updateUsername(
            String newUsername,
            String oldUsername
    ) {}

    public record updateGender(
            String newGender,
            String oldGender
    ) {}



}
