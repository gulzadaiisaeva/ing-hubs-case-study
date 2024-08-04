package com.ing.hubs.case_study.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal currentPrice;

}