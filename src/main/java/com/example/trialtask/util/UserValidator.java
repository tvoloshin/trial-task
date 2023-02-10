package com.example.trialtask.util;

import com.example.trialtask.models.User;
import com.example.trialtask.services.UsersService;
import com.example.trialtask.util.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UsersService usersService;

    @Autowired
    public UserValidator(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User person = (User) target;

        try {
            usersService.findByUsername(person.getUsername());
        }
        catch (UserNotFoundException ignored) {
            return;
        }

        errors.rejectValue("username", "", "Username already exists");
    }
}
