package com.example.trialtask.services;

import com.example.trialtask.models.Quote;
import com.example.trialtask.models.User;
import com.example.trialtask.models.Vote;
import com.example.trialtask.repositories.QuotesRepository;
import com.example.trialtask.repositories.UsersRepository;
import com.example.trialtask.repositories.VotesRepository;
import com.example.trialtask.util.exceptions.QuoteNotFoundException;
import com.example.trialtask.util.exceptions.UserNotFoundException;
import com.example.trialtask.util.exceptions.VotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VotesService {
    private final VotesRepository votesRepository;
    private final QuotesRepository quotesRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public VotesService(VotesRepository votesRepository, QuotesRepository quotesRepository,
                        UsersRepository usersRepository) {
        this.votesRepository = votesRepository;
        this.quotesRepository = quotesRepository;
        this.usersRepository = usersRepository;
    }

    @Transactional
    public void newVote(int userId, int quoteId, int voteScore) {
        User user = usersRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        int currentVoteScore = votesRepository.findByUserId(userId).stream().map(Vote::getScore).reduce(0, Integer::sum);
        if (currentVoteScore == voteScore)
            throw new VotingException();

        Quote quote = quotesRepository.findById(quoteId).orElseThrow(QuoteNotFoundException::new);
        quote.setScore(quote.getScore() + voteScore);

        votesRepository.save(new Vote(user, quote, voteScore));
    }
}
