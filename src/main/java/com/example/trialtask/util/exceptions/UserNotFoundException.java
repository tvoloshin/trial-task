package com.example.trialtask.util.exceptions;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException() {
        super("User with this ID wasn't found");
    }
}
