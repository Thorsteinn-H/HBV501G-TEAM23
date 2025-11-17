package is.hi.hbv501gteam23.Persistence.Specifications;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class MatchSpecification {


    public static Specification<Match> matchDate(LocalDate startDate, LocalDate endDate){
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) return null;
            if (startDate == null) return criteriaBuilder.lessThanOrEqualTo(root.get("date"), endDate);
            if (endDate == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("date"), startDate);
            return criteriaBuilder.between(root.get("date"),startDate, endDate);

        };
    }

    public static Specification<Match> matchHomeGoals(Integer goals){
        return (root, query, criteriaBuilder) ->
                (goals == null||goals<0) ? null:
                criteriaBuilder.greaterThanOrEqualTo(root.get("goals"), goals);

    }

    public static Specification<Match> matchAwayGoals(Integer goals){
        return (root, query, criteriaBuilder) ->
                (goals == null||goals<0) ? null:
                criteriaBuilder.greaterThanOrEqualTo(root.get("goals"), goals);

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
