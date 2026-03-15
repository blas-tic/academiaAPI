package com.example.academia.exception;

public class InvalidPersonTypeException extends RuntimeException {
    public InvalidPersonTypeException(String message) {
        super(message);
    }
}