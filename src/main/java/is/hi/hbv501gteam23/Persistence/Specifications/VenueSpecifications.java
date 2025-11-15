package is.hi.hbv501gteam23.Persistence.Specifications;

import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import org.springframework.data.jpa.domain.Specification;

public class VenueSpecifications {

    public static Specification<Venue> hasId(Long id) {
        return (root, query, builder) -> (id == null) ? null : builder.equal(root.get("id"), id);
    }

    public static Specification<Venue> hasName(String name) {
        return (root, query, builder) ->
                (name == null || name.isBlank()) ? null :
                        builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Venue> hasAddress(String address) {
        return (root, query, builder) ->
                (address == null || address.isBlank()) ? null :
                        builder.like(builder.lower(root.get("address")), "%" + address.toLowerCase() + "%");
    }
}
