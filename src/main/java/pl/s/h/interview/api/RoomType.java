package pl.s.h.interview.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum RoomType {

    PREMIUM(100),
    ECONOMY(1);

    private final int priority;

    RoomType(int priority) {
        this.priority = priority;
    }

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
