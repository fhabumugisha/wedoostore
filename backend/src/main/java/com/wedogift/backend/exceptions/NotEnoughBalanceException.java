package com.wedogift.backend.exceptions;


public class NotEnoughBalanceException extends RuntimeException {

    public NotEnoughBalanceException(String message) {
        super(message);
    }

    public NotEnoughBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}

