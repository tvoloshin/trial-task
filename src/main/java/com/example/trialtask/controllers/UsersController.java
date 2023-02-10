package com.example.trialtask.controllers;

import com.example.trialtask.dto.QuoteDTO;
import com.example.trialtask.dto.QuotesResponse;
import com.example.trialtask.dto.UserDTO;
import com.example.trialtask.models.User;
import com.example.trialtask.services.QuotesService;
import com.example.trialtask.services.UsersService;
import com.example.trialtask.util.ErrorMessage;
import com.example.trialtask.dto.ErrorResponse;
import com.example.trialtask.util.exceptions.UserNotSavedException;
import com.example.trialtask.util.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;
    private final QuotesService quotesService;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;

    @Autowired
    public UsersController(UsersService usersService, QuotesService quotesService, ModelMapper modelMapper, UserValidator userValidator) {
        this.usersService = usersService;
        this.quotesService = quotesService;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
    }

    @GetMapping("/{id}/quotes")
    public QuotesResponse viewQuotes(@PathVariable("id") int id) {
        return new QuotesResponse(quotesService.findAllForUser(id)
                        .stream().map(quote -> modelMapper.map(quote, QuoteDTO.class)).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public UserDTO viewUser(@PathVariable("id") int id) {
        return convertToDTO(usersService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO,
                                              BindingResult bindingResult) {
        User user = convertFromDTO(userDTO);

        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors())
            throw new UserNotSavedException(ErrorMessage.build(bindingResult));

        User createdUser = usersService.register(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(createdUser.getId()).toUri();
        return ResponseEntity.created(uri).body(convertToDTO(createdUser));
    }

    private User convertFromDTO(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UserNotSavedException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
