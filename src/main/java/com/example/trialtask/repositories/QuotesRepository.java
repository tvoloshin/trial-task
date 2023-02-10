package com.example.trialtask.repositories;

import com.example.trialtask.models.Quote;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuotesRepository extends JpaRepository<Quote, Integer> {
    @EntityGraph(attributePaths = "user")
    Optional<Quote> findById(int id);

    List<Quote> findTop10ByOrderByScoreDesc();

    List<Quote> findTop10ByOrderByScoreAsc();

    @Query("from Quote order by rand()")
    @EntityGraph(attributePaths = "user")
    List<Quote> findInRandomOrder(Pageable pageable);
}
