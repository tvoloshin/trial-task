package com.example.trialtask.controllers;

import com.example.trialtask.services.VotesService;
import com.example.trialtask.dto.ErrorResponse;
import com.example.trialtask.util.exceptions.VotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quotes")
public class VotesController {
    private final VotesService votesService;

    @Autowired
    public VotesController(VotesService votesService) {
        this.votesService = votesService;
    }

    @PostMapping("/{id}/upvote")
    public ResponseEntity<HttpStatus> upvote(@PathVariable("id") int id, @RequestHeader("user-id") int userId) {
        votesService.newVote(userId, id, 1);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{id}/downvote")
    public ResponseEntity<HttpStatus> downvote(@PathVariable("id") int id, @RequestHeader("user-id") int userId) {
        votesService.newVote(userId, id, -1);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(VotingException e) {
        return new ResponseEntity<>(new ErrorResponse("Up or down voting more than once"), HttpStatus.BAD_REQUEST);
    }
}
