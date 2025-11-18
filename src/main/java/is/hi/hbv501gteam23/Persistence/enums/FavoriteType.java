package is.hi.hbv501gteam23.Persistence.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum FavoriteType {
    PLAYER("Player"),
    TEAM("Team"),
    MATCH("Match");

    private final String label;

    FavoriteType(String label) {
        this.label = label;
    }

    @JsonCreator
    public static FavoriteType fromString(String value) {
        return value == null ? null : FavoriteType.valueOf(value.toUpperCase());
    }
}
