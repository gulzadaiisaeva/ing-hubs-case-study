package com.ing.hubs.case_study.repository;

import com.ing.hubs.case_study.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query("SELECT s FROM Stock s WHERE LOWER(s.name) = LOWER(:name)")
    Optional<Stock> findByName(String name);
}