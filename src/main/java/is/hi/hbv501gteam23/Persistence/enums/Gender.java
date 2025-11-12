package is.hi.hbv501gteam23.Persistence.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    @JsonCreator
    public static Gender fromString(String value) {
        return value == null ? null : Gender.valueOf(value.toUpperCase());
    }
}
