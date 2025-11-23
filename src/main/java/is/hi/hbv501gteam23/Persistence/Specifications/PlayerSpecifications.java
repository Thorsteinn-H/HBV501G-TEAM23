package is.hi.hbv501gteam23.Persistence.Specifications;

import is.hi.hbv501gteam23.Persistence.Entities.Player;
import org.springframework.data.jpa.domain.Specification;

public class PlayerSpecifications {

    public static Specification<Player> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Player> hasTeamId(Long teamId) {
        return (root, query, cb) -> {
            if (teamId == null) return null;
            return cb.equal(root.get("team").get("id"), teamId);
        };
    }

    public static Specification<Player> hasTeamName(String teamName) {
        return (root, query, cb) -> {
            if (teamName == null || teamName.isBlank()) return null;
            return cb.like(cb.lower(root.get("team").get("name")), "%" + teamName.toLowerCase() + "%");
        };
    }

    public static Specification<Player> hasCountry(String countryCode) {
        return (root, query, cb) -> {
            if (countryCode == null || countryCode.isBlank()) return null;
            return cb.equal(root.get("country").get("code"), countryCode.toUpperCase());
        };
    }

    public static Specification<Player> isActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) return null;
            return cb.equal(root.get("active"), active);
        };
    }
}