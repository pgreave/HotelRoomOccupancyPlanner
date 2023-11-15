package pl.s.h.interview.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum RoomType {

    PREMIUM,
    ECONOMY;

    @JsonCreator
    public static RoomType fromValue(String value) {
        return Arrays.stream(RoomType.values())
                .filter(roomType -> roomType.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }

    @JsonValue
    public String getValue() {
        return this.name();
    }
}
