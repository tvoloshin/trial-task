package com.example.trialtask.util.exceptions;

public abstract class EntityNotSavedException extends RuntimeException{
    public EntityNotSavedException(String message) {
        super(message);
    }
}
