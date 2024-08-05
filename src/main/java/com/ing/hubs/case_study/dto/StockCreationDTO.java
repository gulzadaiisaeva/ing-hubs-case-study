package com.ing.hubs.case_study.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class StockCreationDTO {


    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    @NotNull(message = "Current price is required")
    private BigDecimal currentPrice;
}
