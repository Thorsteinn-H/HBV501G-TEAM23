package is.hi.hbv501gteam23.Persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import is.hi.hbv501gteam23.Persistence.Entities.Image;
import is.hi.hbv501gteam23.Persistence.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public final class ProfileDto {

    /**
     * Request body for updating the authenticated user's profile.
     * Only fields that are not null will be updated.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record UpdateProfileRequest(
        @Schema(example = "newUsername")
        String username,

        @Schema(example = "MALE")
        Gender gender
    ) {}

    /**
     * Request body for updating own password
     *
     * @param newPassword The new password for the user
     * @param oldPassword The old password for the user
     */
    public record UpdatePassword(
            @Schema(example = "newPassword456")
            @NotBlank(message = "New password is required")
            @Size(min = 8, message = "New password must be at least 8 characters")
            String newPassword,

            @Schema(example = "oldPassword123")
            @NotBlank(message = "Old password is required")
            String oldPassword
    ) {}

    /**
     * Response body for returning the authenticated user's profile.
     * Does not include admin-only fields like SystemRole or isActive.
     */
    public record ProfileResponse(
            Long id,

            @Schema(example = "user@team23.com")
            String email,

            @Schema(example = "username")
            String username,

            @Schema(example = "FEMALE")
            Gender gender,

            LocalDateTime createdAt,
            Image profileImage
    ) {}
}
