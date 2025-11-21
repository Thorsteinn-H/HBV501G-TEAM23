package is.hi.hbv501gteam23.Persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;

public final class MatchDto {

    /**
     * Request body for creating a new match
     * All fields are required for POST.
     * @param matchDate when the match took place
     * @param homeTeamId the id of the home team
     * @param awayTeamId the id of the away team
     * @param venueId the id of the home teams venue
     * @param homeGoals home teams goals
     * @param awayGoals away teams goals
     */
    public record CreateMatchRequest(
            @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
            OffsetDateTime matchDate,
            Long homeTeamId,
            Long awayTeamId,
            Long venueId,
            Integer homeGoals,
            Integer awayGoals
    ) {}

    /**
     * Request body for updating a match.
     * All fields are optional. PATCH only updates non-null fields.
     *
     * @param matchDate when the match took place
     * @param homeTeamId the id of the home team
     * @param awayTeamId the id of the away team
     * @param venueId the id of the home teams venue
     * @param homeGoals home teams goals
     * @param awayGoals away teams goals
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record PatchMatchRequest(
            OffsetDateTime matchDate,
            Long homeTeamId,
            Long awayTeamId,
            Long venueId,
            Integer homeGoals,
            Integer awayGoals
    ) {}

    /**
     * Request body for retrieving a match
     *
     * @param id the id of the match
     * @param matchDate when the match took place
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
            OffsetDateTime matchDate,
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
