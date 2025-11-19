package is.hi.hbv501gteam23.Persistence.Specifications;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class MatchSpecifications {

    public static Specification<Match> matchDate(LocalDate startDate, LocalDate endDate){
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) return null;
            if (startDate == null) return criteriaBuilder.lessThanOrEqualTo(root.get("matchDate"), endDate);
            if (endDate == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("matchDate"), startDate);
            return criteriaBuilder.between(root.get("matchDate"),startDate, endDate);
        };
    }

    public static Specification<Match> matchHomeGoals(Integer goals){
        return (root, query, criteriaBuilder) ->
            (goals == null || goals < 0) ? null:
            criteriaBuilder.greaterThanOrEqualTo(root.get("homeGoals"), goals);
    }

    public static Specification<Match> matchAwayGoals(Integer goals){
        return (root, query, criteriaBuilder) ->
            (goals == null|| goals < 0) ? null:
            criteriaBuilder.greaterThanOrEqualTo(root.get("awayGoals"), goals);
    }

    public static Specification<Match> matchHomeTeamName(String homeTeamName){
        return (root, query, criteriaBuilder) ->
            (homeTeamName == null || homeTeamName.isBlank()) ? null:
            criteriaBuilder.like(criteriaBuilder.lower(root.get("homeTeam").get("name")), "%" + homeTeamName.toLowerCase() + "%");
    }

    public static Specification<Match> matchAwayTeamName(String awayTeamName){
        return (root, query, criteriaBuilder) ->
            (awayTeamName == null || awayTeamName.isBlank()) ? null:
            criteriaBuilder.like(criteriaBuilder.lower(root.get("awayTeam").get("name")), "%" + awayTeamName.toLowerCase() + "%");
    }

    public static Specification<Match> matchVenueName(String name){
        return (root, query, criteriaBuilder) ->
            (name == null || name.isBlank()) ? null:
            criteriaBuilder.like(criteriaBuilder.lower(root.get("venue").get("name")), "%" + name.toLowerCase() + "%");
    }
}
