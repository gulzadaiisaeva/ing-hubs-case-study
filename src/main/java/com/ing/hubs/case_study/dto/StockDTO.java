package com.ing.hubs.case_study.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class StockDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal currentPrice;

}