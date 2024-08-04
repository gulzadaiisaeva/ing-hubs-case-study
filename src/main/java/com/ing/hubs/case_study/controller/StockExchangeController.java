package com.ing.hubs.case_study.controller;

import com.ing.hubs.case_study.dto.StockAdditionDTO;
import com.ing.hubs.case_study.dto.StockExchangeCreationDTO;
import com.ing.hubs.case_study.dto.StockExchangeDTO;
import com.ing.hubs.case_study.exception.CaseStudyException;
import com.ing.hubs.case_study.service.StockExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stock-exchange")
public class StockExchangeController {

    private final StockExchangeService stockExchangeService;

    @GetMapping("/{name}")
    public StockExchangeDTO getStockExchange(@PathVariable String name) throws CaseStudyException {
        return stockExchangeService.getStockExchangeByName(name);
    }

    @PostMapping
    public StockExchangeDTO createStockExchange(@RequestBody StockExchangeCreationDTO stockExchangeDTO) throws CaseStudyException {
        return stockExchangeService.createStockExchange(stockExchangeDTO);
    }

    @PostMapping("/{name}")
    public void addStockToExchange(@PathVariable String name, @RequestBody StockAdditionDTO stockDTO) throws CaseStudyException {
        stockExchangeService.addStockToExchange(name, stockDTO);
    }

    @DeleteMapping("/{exchangeName}/{stockName}")
    public void deleteStockFromExchange(@PathVariable String exchangeName, @PathVariable String stockName) throws CaseStudyException {
        stockExchangeService.deleteStockFromExchange(exchangeName, stockName);
    }
}