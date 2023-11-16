package pl.s.h.interview.web.validator;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;
import pl.s.h.interview.api.OccupancyPlanBuildRequest;
import pl.s.h.interview.api.RoomAvailability;
import pl.s.h.interview.api.RoomType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Component
public class OccupancyPlanBuildRequestValidator implements Consumer<OccupancyPlanBuildRequest> {

    private static final Predicate<Map.Entry<RoomType, Long>> REPEATED_ROOM_TYPE_PREDICATE = entry -> entry.getValue() > 1;

    @Override
    public void accept(OccupancyPlanBuildRequest request) {
        requireNonNull(request, "Request cannot be null");

        validateRoomAvailabilities(request);
    }

    private static void validateRoomAvailabilities(OccupancyPlanBuildRequest request) {
        final Map<RoomType, Long> roomTypeOccurrences = request.roomAvailabilities().stream()
                .map(RoomAvailability::type)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        final List<RoomType> repeatedRoomTypes = roomTypeOccurrences.entrySet().stream()
                .filter(REPEATED_ROOM_TYPE_PREDICATE)
                .map(Map.Entry::getKey)
                .toList();

        Optional.of(repeatedRoomTypes)
                .filter(roomTypes -> !roomTypes.isEmpty())
                .ifPresent(roomTypes -> {
                    throw new ValidationException("Request contain duplicated room availabilities for room types: " + repeatedRoomTypes);
                });
    }
}
