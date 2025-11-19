package is.hi.hbv501gteam23.Persistence.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum PlayerPosition {
    GOALKEEPER("Goalkeeper"),
    DEFENDER("Defender"),
    MIDFIELDER("Midfielder"),
    FORWARD("Forward");

    private final String label;

    PlayerPosition(String label) {
        this.label = label;
    }

    @JsonCreator
    public static PlayerPosition fromString(String value) {
        return value == null ? null : PlayerPosition.valueOf(value.toUpperCase());
    }
}
