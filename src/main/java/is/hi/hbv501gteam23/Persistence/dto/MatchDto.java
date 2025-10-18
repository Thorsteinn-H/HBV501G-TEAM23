package is.hi.hbv501gteam23.Persistence.dto;
import java.time.LocalDate;

public class MatchDto {
    public record CreateMatchRequest(
            LocalDate date,
            Long homeTeamId,
            Long awayTeamId,
            Long venueId,
            Integer homeGoals,
            Integer awayGoals
    ) {}

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
