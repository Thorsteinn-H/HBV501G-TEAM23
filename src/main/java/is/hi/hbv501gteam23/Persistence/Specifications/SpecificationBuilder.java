package is.hi.hbv501gteam23.Persistence.Specifications;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationBuilder<T> {

    private Specification<T> spec = null;

    public SpecificationBuilder<T> and(Specification<T> other) {
        if (other != null) {
            spec = (spec == null) ? other : spec.and(other);
        }
        return this;
    }

    public Specification<T> build() {
        return spec;
    }
}