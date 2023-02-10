package com.example.trialtask.controllers;

import com.example.trialtask.dto.ChartResponse;
import com.example.trialtask.dto.ErrorResponse;
import com.example.trialtask.dto.QuoteDTO;
import com.example.trialtask.dto.QuotesResponse;
import com.example.trialtask.models.Quote;
import com.example.trialtask.services.QuotesService;
import com.example.trialtask.util.*;
import com.example.trialtask.util.exceptions.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quotes")
public class QuotesController {
    private final QuotesService quotesService;
    private final ModelMapper modelMapper;

    @Autowired
    public QuotesController(QuotesService quotesService, ModelMapper modelMapper) {
        this.quotesService = quotesService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/top")
    public QuotesResponse findTop10() {
        return new QuotesResponse(quotesService.findTop10().stream().map(this::convertToDTO).collect(Collectors.toList()));
    }

    @GetMapping("/worst")
    public QuotesResponse findWorst10() {
        return new QuotesResponse(quotesService.findWorst10().stream().map(this::convertToDTO).collect(Collectors.toList()));
    }

    @GetMapping("/random")
    public QuotesResponse findRandom() {
        return new QuotesResponse(Collections.singletonList(convertToDTO(quotesService.findRandom())));
    }

    @GetMapping("/{id}")
    public QuotesResponse view(@PathVariable("id") int id) {
        return new QuotesResponse(Collections.singletonList(convertToDTO(quotesService.findById(id))));
    }


    @GetMapping("/{id}/chart")
    public ResponseEntity<Object> chart(@PathVariable("id") int id) {
        return new ResponseEntity<>(new ChartResponse(quotesService.votesGraph(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<QuoteDTO> create(@RequestHeader("user-id") int userId,
                                           @RequestBody @Valid QuoteDTO quoteDTO,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new QuoteNotSavedException(ErrorMessage.build(bindingResult));

        Quote quote = quotesService.addForUser(userId, convertFromDTO(quoteDTO));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(quote.getId()).toUri();
        return ResponseEntity.created(uri).body(convertToDTO(quote));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestHeader("user-id") int userId,
                                             @RequestBody @Valid QuoteDTO quoteDTO,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new QuoteNotSavedException(ErrorMessage.build(bindingResult));

        quoteDTO.setId(id);
        quotesService.updateForUser(userId, convertFromDTO(quoteDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id,
                                             @RequestHeader("user-id") int userId) {
        quotesService.deleteForUser(userId, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Quote convertFromDTO(QuoteDTO quoteDTO) {
        return modelMapper.map(quoteDTO, Quote.class);
    }

    private QuoteDTO convertToDTO(Quote quote) {
        return modelMapper.map(quote, QuoteDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(EntityNotSavedException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(EntityNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MissingRequestHeaderException e) {
        return new ResponseEntity<>(new ErrorResponse("Field user-id should be provided in request header"), HttpStatus.BAD_REQUEST);
    }
}
