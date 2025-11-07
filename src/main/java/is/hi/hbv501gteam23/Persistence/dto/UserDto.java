package is.hi.hbv501gteam23.Persistence.dto;

import java.time.LocalDate;

public final class UserDto {

    public record UserResponse(
            Long id,
            String name,
            String email,
            String gender,
            LocalDate createdAt,
            String passwordHash,
            String role

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
