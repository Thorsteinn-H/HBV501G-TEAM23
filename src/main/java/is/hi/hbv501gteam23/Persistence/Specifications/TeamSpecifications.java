package is.hi.hbv501gteam23.Persistence.Specifications;

import is.hi.hbv501gteam23.Persistence.Entities.Team;
import org.springframework.data.jpa.domain.Specification;


public class TeamSpecifications {

    public static Specification<Team> teamId(Long id){
        return (root, query, criteriaBuilder) ->
                (id == null) ? null:
                        criteriaBuilder.equal(root.get("id"), id);

    }

    public static Specification<Team> teamName(String name) {
        return (root, query, criteriaBuilder) ->
                (name == null || name.isBlank()) ? null:
                        criteriaBuilder.like(root.get("name"), name);
    }

    public static Specification<Team> teamStatus(Boolean isActive) {
        return (root, query, criteriaBuilder) ->
                (isActive == null) ? null:
                criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<Team> teamCountry(String country) {
        return (root, query, criteriaBuilder) ->
                (country == null || country.isBlank()) ? null:
                        criteriaBuilder.like(root.get("country"), country);
    }

    public static Specification<Team> teamVenue(Long id){
        return (root, query, criteriaBuilder) ->
                (id == null) ? null:
                        criteriaBuilder.equal(root.get("venue"), id);

    }

    public static Specification<Team> teamVenueName(String venueName){
        return (root, query, criteriaBuilder) ->
                (venueName == null || venueName.isBlank()) ? null:
                        criteriaBuilder.like(root.get("venue").get("name"), venueName);

    }

}
