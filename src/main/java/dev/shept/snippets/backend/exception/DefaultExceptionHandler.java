package dev.shept.snippets.backend.exception;

import dev.shept.snippets.backend.exception.impl.NullValueException;
import dev.shept.snippets.backend.exception.impl.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ApiError> handleException(Exception exception,
                                                    HttpServletRequest request) {
        ApiError error = new ApiError(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullValueException.class)
    public ResponseEntity<ApiError> handleException(NullValueException exception,
                                                    HttpServletRequest request) {
        ApiError error = new ApiError(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleException(ResourceNotFoundException exception,
                                                    HttpServletRequest request) {
        ApiError error = new ApiError(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
