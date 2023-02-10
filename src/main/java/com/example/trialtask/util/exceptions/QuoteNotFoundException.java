package com.example.trialtask.util.exceptions;

public class QuoteNotFoundException extends EntityNotFoundException {
    public QuoteNotFoundException() {
        super("Quote with this ID wasn't found");
    }

    public QuoteNotFoundException(String message) {
        super(message);
    }
}
