package dev.shept.snippets.backend.exception.impl;

public class NullValueException extends RuntimeException {

    public NullValueException(Object value) {
        super("Value " + value.getClass().getName() + " cannot be null");
    }
}