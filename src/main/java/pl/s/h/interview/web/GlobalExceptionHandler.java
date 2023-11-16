package pl.s.h.interview.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.s.h.interview.api.ErrorResponse;
import pl.s.h.interview.api.ServiceError;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String UNEXPECTED_ERROR_CODE = "80000";
    private static final String UNHANDLED_ERROR_CODE = "80001";
    private static final String VALIDATION_ERROR_CODE = "70000";

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus
    public ResponseEntity<ErrorResponse> globalExceptionHandler(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .error(buildServiceError(UNEXPECTED_ERROR_CODE, exception.getMessage()))
                        .build());
    }

    @Override
    protected ResponseEntity<Object> createResponseEntity(
            @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return ResponseEntity
                .status(statusCode)
                .body(ErrorResponse.builder()
                        .error(buildServiceError(UNHANDLED_ERROR_CODE, getMessageBody(body)))
                        .build());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        final List<ServiceError> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(this::buildFieldErrorMessage)
                .map(s -> buildServiceError(VALIDATION_ERROR_CODE, s))
                .toList();

        return ResponseEntity
                .status(status)
                .body(ErrorResponse.builder()
                        .errors(errors)
                        .build());
    }

    private ServiceError buildServiceError(String errorCode, String errorMessage) {
        return ServiceError.builder()
                .code(errorCode)
                .message(errorMessage)
                .build();
    }

    private String buildFieldErrorMessage(FieldError fieldError) {
        return String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage());
    }

    private String getMessageBody(Object body) {
        if (body instanceof ProblemDetail) {
            return ((ProblemDetail) body).getDetail();
        } else if (body instanceof org.springframework.web.ErrorResponse) {
            return ((org.springframework.web.ErrorResponse) body).getDetailMessageCode();
        }
        return null;
    }
}
