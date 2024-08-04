package com.ing.hubs.case_study.dto;


import lombok.Data;

import java.util.Set;

@Data
public class StockExchangeDTO {
    private Long id;
    private String name;
    private String description;
    private boolean liveInMarket;
    private Set<StockDTO> stocks;
}