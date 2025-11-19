package is.hi.hbv501gteam23.Config;

import jakarta.persistence.EntityManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class HibernateFilterActivator extends OncePerRequestFilter {

    private final EntityManager entityManager;

    public HibernateFilterActivator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("activeUserFilter");
        filter.setParameter("isActive", true);

        try {
            filterChain.doFilter(request, response);
        } finally {
            session.disableFilter("activeUserFilter");
        }
    }
}
