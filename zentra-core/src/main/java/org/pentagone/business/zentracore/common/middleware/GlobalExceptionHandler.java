package org.pentagone.business.zentracore.common.middleware;

import org.pentagone.business.zentracore.common.exception.EntityNotFoundException;
import org.pentagone.business.zentracore.common.exception.UnauthorisedAccessException;
import org.pentagone.business.zentracore.common.util.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 400 Bad Request
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiError> illegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 401 Unauthorized
     */
    @ExceptionHandler({UnauthorisedAccessException.class})
    public ResponseEntity<ApiError> unauthorisedAccessException(UnauthorisedAccessException e) {
        return new ResponseEntity<>(new ApiError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    /**
     * 404 Not Found
     */
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ApiError> entityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * 500 Internal Server Error
     */
    @SuppressWarnings("CallToPrintStackTrace")
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiError> exception(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
