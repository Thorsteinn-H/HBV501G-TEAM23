package is.hi.hbv501gteam23.Persistence.Specifications;

import is.hi.hbv501gteam23.Persistence.Entities.Team;
import org.springframework.data.jpa.domain.Specification;


public class TeamSpecifications {

    public static Specification<Team> nameContains(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank()) ? null:
                        cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Team> hasActiveStatus(Boolean isActive) {
        return (root, query, cb) ->
                (isActive == null) ? null:
                cb.equal(root.get("isActive"), isActive);
    }

    public static Specification<Team> hasCountry(String country) {
        return (root, query, cb) ->
                (country == null || country.isBlank()) ? null:
                        cb.like(cb.lower(root.get("country")), "%" + country.toLowerCase() + "%");
    }

    public static Specification<Team> venueNameContains(String venueName){
        return (root, query, cb) ->
                (venueName == null || venueName.isBlank()) ? null:
                        cb.like(cb.lower(root.get("venue").get("name")), "%" + venueName.toLowerCase() + "%");
    }
}
