package com.nexus.app.user.exception;

public class ResourceNotFoundException extends RuntimeException {

     public ResourceNotFoundException(String message) {
        super(message);
    }
}
