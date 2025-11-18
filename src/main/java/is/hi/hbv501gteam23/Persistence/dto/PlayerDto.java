package is.hi.hbv501gteam23.Persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import is.hi.hbv501gteam23.Persistence.enums.PlayerPosition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public final class PlayerDto {

    /**
     * Request body for creating a new player
     *
     * This DTO is used in POST request to create a player with a name, date of birth, country, position,
     *      goals and teamID
     *
     * @param name the name of the player
     * @param dateOfBirth the matchDate of players birth
     * @param country players country of origin
     * @param position the position a players plays for the team
     * @param isActive    whether the player is currently active
     * @param goals how many goals a player has scored
     * @param teamId the ID of a team the player belongs too
     */
    public record CreatePlayerRequest(
            @NotBlank
            String name,

            @NotNull
            LocalDate dateOfBirth,

            @NotBlank
            String country,

            @NotNull
            @Schema(enumAsRef = true)
            PlayerPosition position,

            @NotNull
            Boolean isActive,

            Integer goals,
            Long teamId
    ) {}

    /**
     * Update body for updating player
     *
     * All fields are optional; only non-null values will be applied.
     *      Typical uses include renaming a team, changing the country,
     *      or reassigning the team to a different venue.
     *
     * @param name the name of the player
     * @param dateOfBirth the matchDate of players birth
     * @param country players country of origin
     * @param position the position a players plays for the team
     * @param goals how many goals a player has scored
     * @param isActive    whether the player is currently active
     * @param teamId the ID of a team the player belongs too
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record PatchPlayerRequest(
            String name,
            LocalDate dateOfBirth,
            String country,
            PlayerPosition position,
            Integer goals,
            Boolean isActive,
            Long teamId
    ) {}

    /**
     * Request body for retrieving a player
     *
     * This DTO is used in GET request to get a player with an id, name, position, goals,
     *     country, date of birth, teamID and teamName
     *
     * @param id the id of the player
     * @param name the name of the player
     * @param isActive whether the player is currently active
     * @param position the position a players plays for the team
     * @param goals how many goals a player has scored
     * @param country players country of origin
     * @param dateOfBirth the matchDate of players birth
     * @param teamId the ID of a team the player belongs too
     * @param teamName the name of a team the player belongs too
     */
    public record PlayerResponse(
            Long id,
            String name,
            Boolean isActive,
            PlayerPosition position,
            Integer goals,
            String country,
            LocalDate dateOfBirth,
            Long teamId,
            String teamName
    ) {}
}
