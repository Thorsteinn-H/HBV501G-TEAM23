package is.hi.hbv501gteam23.Persistence.dto;

import is.hi.hbv501gteam23.Persistence.Entities.Player;

import java.time.LocalDate;

public final class PlayerDto {

    /**
     * Request body for creating a new player
     *
     * <p>
     *      This DTO is used in POST request to create a player with a name, date of birth, country, position,
     *      goals and teamID
     * </p>
     * @param name the name of the player
     * @param dateOfBirth the date of players birth
     * @param country players country of origin
     * @param position the position a players plays for the team
     * @param goals how many goals a player has scored
     * @param teamId the ID of a team the player belongs too
     */
    public record CreatePlayerRequest(
            String name,
            LocalDate dateOfBirth,
            String country,
            Player.PlayerPosition position,
            Integer goals,
            Long teamId
    ) {}

    /**
     * Request body for retrieving a player
     *
     * <p>
     *     This DTO is used in GET request to get a player with an id, name, position, goals,
     *     country, date of birth, teamID and teamName
     * </p>
     * @param id the id of the player
     * @param name the name of the player
     * @param position the position a players plays for the team
     * @param goals how many goals a player has scored
     * @param country players country of origin
     * @param dateOfBirth the date of players birth
     * @param teamId the ID of a team the player belongs too
     * @param teamName the name of a team the player belongs too
     */
    public record PlayerResponse(
            Long id,
            String name,
            Player.PlayerPosition position,
            Integer goals,
            String country,
            LocalDate dateOfBirth,
            Long teamId,
            String teamName
    ) {}
}
