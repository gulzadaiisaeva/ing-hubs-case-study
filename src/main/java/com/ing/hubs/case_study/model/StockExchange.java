package com.ing.hubs.case_study.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;


@Entity
@Data
public class StockExchange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    private boolean liveInMarket = false;

    @ManyToMany
    @JoinTable(name = "stock_exchange_stock",
            joinColumns = @JoinColumn(name = "stock_exchange_id"),
            inverseJoinColumns = @JoinColumn(name = "stock_id"))
    private Set<Stock> stocks;
}