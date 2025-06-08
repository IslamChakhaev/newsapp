package com.example.springbootnewsportal.exception;

/**
 * Исключение, выбрасываемое при попытке изменить ресурс,
 * владельцем которого не является текущий пользователь.
 */
public class OwnershipViolationException extends RuntimeException {
    public OwnershipViolationException(String message) {
        super(message);
    }
}
