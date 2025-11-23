package is.hi.hbv501gteam23.Persistence.Specifications;

import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import org.springframework.data.jpa.domain.Specification;

public class VenueSpecifications {

    public static Specification<Venue> nameContains(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank()) ? null :
                        cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Venue> addressContains(String address) {
        return (root, query, cb) ->
                (address == null || address.isBlank()) ? null :
                        cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%");
    }
}
