package pl.s.h.interview.api;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

/**
 * Represents single service error with <code>code</code> and <code>message</code>
 *
 * @param code    categorized error identification code
 * @param message short error description
 */
@Builder
public record ServiceError(

        String code,

        @NotEmpty
        String message) {
}
