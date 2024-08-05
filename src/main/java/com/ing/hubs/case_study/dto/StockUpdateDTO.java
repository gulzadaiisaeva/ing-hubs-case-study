package com.ing.hubs.case_study.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class StockUpdateDTO {
    private String name;
    private BigDecimal updatedPrice;

}