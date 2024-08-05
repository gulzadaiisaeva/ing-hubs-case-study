package com.ing.hubs.case_study.controller;

import com.ing.hubs.case_study.dto.StockCreationDTO;
import com.ing.hubs.case_study.dto.StockDTO;
import com.ing.hubs.case_study.dto.StockUpdateDTO;
import com.ing.hubs.case_study.exception.CaseStudyException;
import com.ing.hubs.case_study.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stock")
public class StockController {

    private final StockService stockService;

    @GetMapping
    public List<StockDTO> getAllStocks() {
        return stockService.getAllStocks();
    }

    @PostMapping
    public StockDTO createStock(@RequestBody StockCreationDTO stockDTO) throws CaseStudyException {
        return stockService.createStock(stockDTO);
    }

    @PutMapping
    public StockDTO updateStock(@RequestBody StockUpdateDTO stockDTO) throws CaseStudyException {
        return stockService.updateStock(stockDTO);
    }

    @DeleteMapping("/{name}")
    public void deleteStock(@PathVariable String name) throws CaseStudyException {
        stockService.deleteStock(name);
    }
}