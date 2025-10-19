package is.hi.hbv501gteam23.Persistence.dto;
import java.time.LocalDate;

public class MatchDto {

    /**
     * Request body for creating a new match
     *
     * <p>
     *      This DTO is used in POST request to create a match with date, homeTeamId, awayTeamId, venueId, homeGoals and
     *      awayGoals
     * </p>
     * @param date when the match took place
     * @param homeTeamId the id of the home team
     * @param awayTeamId the id of the away team
     * @param venueId the id of the home teams venue
     * @param homeGoals home teams goals
     * @param awayGoals away teams goals
     */
    public record CreateMatchRequest(
            LocalDate date,
            Long homeTeamId,
            Long awayTeamId,
            Long venueId,
            Integer homeGoals,
            Integer awayGoals
    ) {}

    /**
     * Request body for retrieving a match
     *
     * <p>
     *     This DTO is used in GET request to get a match with an id, date, homeTeamId, homeTeamName, awayTeamId, awayTeamName,
     *     venueId, venueName homeGoals and awayGoals
     * </p>
     *
     * @param id the id of the match
     * @param date when the match took place
     * @param homeTeamId the id of the home team
     * @param homeTeamName the name of the home team
     * @param awayTeamId the id of the away team
     * @param awayTeamName the name of the away team
     * @param venueId the id of the home teams venue
     * @param venueName the name of the home teams venue
     * @param homeGoals home teams goals
     * @param awayGoals away teams goals
     */
    public record MatchResponse(
            Long id,
            LocalDate date,
            Long homeTeamId,
            String homeTeamName,
            Long awayTeamId,
            String awayTeamName,
            Long venueId,
            String venueName,
            Integer homeGoals,
            Integer awayGoals
    ) {}
}
