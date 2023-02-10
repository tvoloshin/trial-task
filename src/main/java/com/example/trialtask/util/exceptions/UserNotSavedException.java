package com.example.trialtask.util.exceptions;


public class UserNotSavedException extends EntityNotFoundException {
    public UserNotSavedException(String message) {
        super(message);
    }
}
