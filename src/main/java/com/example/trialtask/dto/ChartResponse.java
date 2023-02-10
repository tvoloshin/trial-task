package com.example.trialtask.dto;

import java.util.List;
import java.util.Map;

public class ChartResponse {
    private final List<Map<String, String>> score;

    public ChartResponse(List<Map<String, String>> score) {
        this.score = score;
    }

    public List<Map<String, String>> getScore() {
        return score;
    }
}
