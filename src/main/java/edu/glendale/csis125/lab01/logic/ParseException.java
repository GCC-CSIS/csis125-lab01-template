package edu.glendale.csis125.lab01.logic;

/** Thrown when a formula cannot be parsed. Reported to the browser as HTTP 400. */
public class ParseException extends RuntimeException {

    public ParseException(String message) {
        super(message);
    }
}
