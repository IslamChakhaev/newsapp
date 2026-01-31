package com.example.springbootnewsportal.exception;

import java.text.MessageFormat;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super(MessageFormat.format("{0} with ID {1} not found", resourceName, id));
    }



    public ResourceNotFoundException(String resourceName, String key) {
        super(MessageFormat.format("{0} with value: {1} not found", resourceName, key));
    }

}
