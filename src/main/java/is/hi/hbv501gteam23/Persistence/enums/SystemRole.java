package is.hi.hbv501gteam23.Persistence.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum SystemRole {
    ADMIN("Administrator"),
    USER("User"),
    MODERATOR("Moderator");

    private final String label;

    SystemRole(String label) {
        this.label = label;
    }

    @JsonCreator
    public static SystemRole fromString(String value) {
        return value == null ? null : SystemRole.valueOf(value.toUpperCase());
    }
}
