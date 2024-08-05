package com.ing.hubs.case_study.repository;

import com.ing.hubs.case_study.model.Stock;
import com.ing.hubs.case_study.model.StockExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface StockExchangeRepository extends JpaRepository<StockExchange, Long> {
    @Query("SELECT e FROM StockExchange e WHERE LOWER(e.name) = LOWER(:name)")
    Optional<StockExchange> findByName(String name);
    Set<StockExchange> findByStocksContains(Stock stock);
}