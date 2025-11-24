package is.hi.hbv501gteam23.Security;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class AuditCleanupFilter extends OncePerRequestFilter {
    private final AuditContext auditContext;

    public AuditCleanupFilter(AuditContext auditContext) {
        this.auditContext = auditContext;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            FilterChain filterChain)
    throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } finally {
            auditContext.clear();
        }
    }
}
