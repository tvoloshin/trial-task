package com.example.trialtask.dto;

import com.example.trialtask.dto.QuoteDTO;

import java.util.List;

public class QuotesResponse {
    private final List<QuoteDTO> quotes;

    public QuotesResponse(List<QuoteDTO> quotes) {
        this.quotes = quotes;
    }

    public List<QuoteDTO> getQuotes() {
        return quotes;
    }
}
