package com.example.trialtask.repositories;

import com.example.trialtask.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotesRepository extends JpaRepository<Vote, Integer> {
    List<Vote> findByUserId(int userId);

    List<Vote> findAllByQuoteIdOrderByCreatedAt(int quoteId);
}
