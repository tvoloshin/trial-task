package com.example.trialtask.util.exceptions;

public class QuoteNotSavedException extends EntityNotFoundException {
    public QuoteNotSavedException(String message) {
        super(message);
    }
}
