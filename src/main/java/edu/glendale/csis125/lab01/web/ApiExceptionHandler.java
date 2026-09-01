package edu.glendale.csis125.lab01.web;

import java.util.Map;

import edu.glendale.csis125.lab01.logic.ParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Turns exceptions into readable JSON instead of a stack trace in the browser.
 *
 * <p>Provided — no changes needed. The UnsupportedOperationException case is the
 * interesting one for you: until you finish the TODOs, the web page will tell you
 * which one it hit rather than failing silently.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<Map<String, String>> handleParseError(ParseException exception) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadInput(IllegalArgumentException exception) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<Map<String, String>> handleUnfinishedWork(UnsupportedOperationException exception) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(Map.of("error", "This part of the lab is not finished yet: " + exception.getMessage()));
    }
}
