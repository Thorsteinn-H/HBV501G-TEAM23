package is.hi.hbv501gteam23.Persistence.Specifications;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class MatchSpecifications {

    public static Specification<Match> matchDate(LocalDate start, LocalDate end){
        return (root, query, cb) -> {
            if (start == null && end == null) return null;

            var path = root.<OffsetDateTime>get("matchDate");
            if (start != null && end != null) {
                OffsetDateTime from = start.atStartOfDay().atOffset(java.time.ZoneOffset.UTC);
                OffsetDateTime toExclusive = end.plusDays(1)
                                                    .atStartOfDay()
                                                    .atOffset(java.time.ZoneOffset.UTC);
                return cb.between(path, from, toExclusive);
            }

            if (start != null) {
                var from = start.atStartOfDay().atOffset(java.time.ZoneOffset.UTC);
                return cb.greaterThanOrEqualTo(path, from);
            }

            var toExclusive = end.plusDays(1)
                                    .atStartOfDay()
                                    .atOffset(java.time.ZoneOffset.UTC);
            return cb.lessThan(path, toExclusive);
        };
    }

    public static Specification<Match> matchHomeGoals(Integer goals){
        return (root, query, cb) ->
            (goals == null || goals < 0) ? null:
            cb.greaterThanOrEqualTo(root.get("homeGoals"), goals);
    }

    public static Specification<Match> matchAwayGoals(Integer goals){
        return (root, query, cb) ->
            (goals == null|| goals < 0) ? null:
            cb.greaterThanOrEqualTo(root.get("awayGoals"), goals);
    }

    public static Specification<Match> matchHomeTeamName(String homeTeamName){
        return (root, query, cb) ->
            (homeTeamName == null || homeTeamName.isBlank()) ? null:
            cb.like(cb.lower(root.get("homeTeam").get("name")), "%" + homeTeamName.toLowerCase() + "%");
    }

    public static Specification<Match> matchAwayTeamName(String awayTeamName){
        return (root, query, cb) ->
            (awayTeamName == null || awayTeamName.isBlank()) ? null:
            cb.like(cb.lower(root.get("awayTeam").get("name")), "%" + awayTeamName.toLowerCase() + "%");
    }

    public static Specification<Match> matchVenueName(String name){
        return (root, query, cb) ->
            (name == null || name.isBlank()) ? null:
            cb.like(cb.lower(root.get("venue").get("name")), "%" + name.toLowerCase() + "%");
    }
}
