package com.example.trialtask.services;

import com.example.trialtask.models.Quote;
import com.example.trialtask.models.User;
import com.example.trialtask.models.Vote;
import com.example.trialtask.repositories.QuotesRepository;
import com.example.trialtask.repositories.UsersRepository;
import com.example.trialtask.repositories.VotesRepository;
import com.example.trialtask.util.exceptions.QuoteNotFoundException;
import com.example.trialtask.util.exceptions.QuoteNotSavedException;
import com.example.trialtask.util.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class QuotesService {
    private final QuotesRepository quotesRepository;
    private final UsersRepository usersRepository;
    private final VotesRepository votesRepository;

    @Autowired
    public QuotesService(QuotesRepository quotesRepository, UsersRepository usersRepository,
                         VotesRepository votesRepository) {
        this.quotesRepository = quotesRepository;
        this.usersRepository = usersRepository;
        this.votesRepository = votesRepository;
    }

    public List<Quote> findTop10() {
        return quotesRepository.findTop10ByOrderByScoreDesc();
    }

    public List<Quote> findWorst10() {
        return quotesRepository.findTop10ByOrderByScoreAsc();
    }

    public Quote findRandom() {
        List<Quote> quotes = quotesRepository.findInRandomOrder(PageRequest.of(0, 1));
        if (quotes.isEmpty())
            throw new QuoteNotFoundException("No quotes were found");
        return quotes.get(0);
    }

    public List<Quote> findAllForUser(int userId) {
        return usersRepository.findById(userId).orElseThrow(UserNotFoundException::new).getQuotes();
    }

    public Quote findById(int id) {
        return quotesRepository.findById(id).orElseThrow(QuoteNotFoundException::new);
    }

    public List<Map<String, String>> votesGraph(int quoteId) {
        Quote quote = quotesRepository.findById(quoteId).orElseThrow(QuoteNotFoundException::new);

        List<Map<String, String>> graphData = new ArrayList<>();
        int totalScore = 0;

        addPoint(graphData, quote.getCreatedAt(), totalScore);
        for (Vote vote: votesRepository.findAllByQuoteIdOrderByCreatedAt(quoteId))
            addPoint(graphData, vote.getCreatedAt(), totalScore += vote.getScore());

        return graphData;
    }

    @Transactional
    public Quote addForUser(int userId, Quote quote) {
        enrichQuote(userId, quote);
        return quotesRepository.save(quote);
    }

    @Transactional
    public void updateForUser(int userId, Quote quote) {
        Quote quoteToUpdate = getQuoteToChange(userId, quote.getId());
        quoteToUpdate.setQuote(quote.getQuote());
    }

    @Transactional
    public void deleteForUser(int userId, int quoteId) {
        Quote quoteToDelete = getQuoteToChange(userId, quoteId);
        quotesRepository.delete(quoteToDelete);
    }

    private void enrichQuote(int userId, Quote quote) {
        User user = usersRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        quote.setUser(user);
    }

    private Quote getQuoteToChange(int userId, int quoteId) {
        if (!usersRepository.existsById(userId))
            throw new UserNotFoundException();

        Quote quoteToChange = quotesRepository.findById(quoteId).orElseThrow(QuoteNotFoundException::new);
        if (userId != quoteToChange.getUser().getId())
            throw new QuoteNotSavedException("Can't change another user's quote");

        return quoteToChange;
    }

    private void addPoint(List<Map<String, String>> graphData, LocalDateTime timestamp, int value) {
        graphData.add(Map.of(
                "timestamp", timestamp.toString(),
                "value", String.valueOf(value)
        ));
    }
}
