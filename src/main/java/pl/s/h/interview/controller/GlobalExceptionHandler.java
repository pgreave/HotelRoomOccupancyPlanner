package pl.s.h.interview.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.s.h.interview.api.ErrorResponse;
import pl.s.h.interview.api.ServiceError;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String UNEXPECTED_ERROR_CODE = "80000";
    private static final String UNHANDLED_ERROR_CODE = "80001";

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus
    public ResponseEntity<ErrorResponse> globalExceptionHandler(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .error(ServiceError.builder()
                                .code(UNEXPECTED_ERROR_CODE)
                                .message(exception.getMessage())
                                .build())
                        .build());
    }

    @Override
    protected ResponseEntity<Object> createResponseEntity(@Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return ResponseEntity
                .status(statusCode)
                .body(ErrorResponse.builder()
                        .error(ServiceError.builder()
                                .code(UNHANDLED_ERROR_CODE)
                                .message(getMessageBody(body))
                                .build())
                        .build());
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
