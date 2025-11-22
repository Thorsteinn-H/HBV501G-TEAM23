package is.hi.hbv501gteam23.Persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
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

    /**
     * Filter parameters for listing matches.
     * <p>
     * All fields are optional. When {@code null}, a field is ignored in the filter.
     * Sorting is controlled by {@code sortBy} and {@code sortDir}.
     *
     * @param startDate    lower bound (inclusive) for the match date
     * @param endDate      upper bound (inclusive) for the match date
     * @param homeGoals    minimum number of goals scored by the home team
     * @param awayGoals    minimum number of goals scored by the away team
     * @param homeTeamName home team name filter (substring, case-insensitive)
     * @param awayTeamName away team name filter (substring, case-insensitive)
     * @param venueName    venue name filter (substring, case-insensitive)
     * @param sortBy       field to sort by (defaults to "id" if null/blank)
     * @param sortDir      sort direction, either "ASC" or "DESC" (defaults to "ASC" if null/blank)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record MatchFilter(
            @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
            LocalDate startDate,
            @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
            LocalDate endDate,
            Integer homeGoals,
            Integer awayGoals,
            String homeTeamName,
            String awayTeamName,
            String venueName,
            String sortBy,
            String sortDir
    ) {}
}
