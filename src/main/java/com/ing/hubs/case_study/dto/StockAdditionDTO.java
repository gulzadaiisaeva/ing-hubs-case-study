package com.ing.hubs.case_study.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAdditionDTO {

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    private String name;
}
