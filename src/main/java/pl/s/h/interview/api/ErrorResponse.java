package pl.s.h.interview.api;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

/**
 * Response describing a list of errors that occurred during processing request
 *
 * @param errors list of occurred errors
 */
@Builder
public record ErrorResponse(
        @NotEmpty
        @Singular
        List<ServiceError> errors) {
}
