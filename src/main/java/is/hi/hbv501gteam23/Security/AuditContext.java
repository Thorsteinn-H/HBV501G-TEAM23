package is.hi.hbv501gteam23.Security;

import org.springframework.stereotype.Component;

/**
 * Tracks the current user for auditing purposes.
 * Supports application-level ThreadLocal tracking.
 */
@Component
public class AuditContext {
    private static final ThreadLocal<Long> currentUser = new ThreadLocal<>();

    public void setCurrentUserId(Long userId) {
        currentUser.set(userId);
    }

    public Long getCurrentUserId() {
        return currentUser.get();
    }

    public void clear() {
        currentUser.remove();
    }
}
