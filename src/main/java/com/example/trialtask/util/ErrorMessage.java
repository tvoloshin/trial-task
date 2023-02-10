package com.example.trialtask.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorMessage {
    public static String build(BindingResult bindingResult) {
        StringBuilder stringBuilder = new StringBuilder();

        List<FieldError> errors = bindingResult.getFieldErrors();

        for (FieldError error: errors) {
            stringBuilder.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append(";");
        }
        return stringBuilder.toString();
    }
}
