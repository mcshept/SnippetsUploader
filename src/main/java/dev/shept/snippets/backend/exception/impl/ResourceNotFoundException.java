package dev.shept.snippets.backend.exception.impl;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Resource not found");
    }
}
