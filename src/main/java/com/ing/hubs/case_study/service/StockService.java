package com.ing.hubs.case_study.service;

import com.ing.hubs.case_study.dto.StockCreationDTO;
import com.ing.hubs.case_study.dto.StockDTO;
import com.ing.hubs.case_study.exception.CaseStudyException;
import com.ing.hubs.case_study.exception.ValidationException;
import com.ing.hubs.case_study.mapper.StockMapper;
import com.ing.hubs.case_study.model.Stock;
import com.ing.hubs.case_study.model.StockExchange;
import com.ing.hubs.case_study.repository.StockExchangeRepository;
import com.ing.hubs.case_study.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final StockExchangeRepository stockExchangeRepository;

    public List<StockDTO> getAllStocks() {
        return stockRepository.findAll().stream()
                .map(StockMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    public StockDTO createStock(StockCreationDTO stockDTO) throws CaseStudyException {
        Optional<Stock> existingStock = stockRepository.findByName(stockDTO.getName());
        if (existingStock.isPresent()) {
            throw new CaseStudyException(String.format("Stock with name %s already exists", stockDTO.getName()));
        }
        Stock stock = StockMapper.INSTANCE.toEntity(stockDTO);
        return StockMapper.INSTANCE.toDTO(stockRepository.save(stock));
    }

    public StockDTO updateStock(StockDTO stockDTO) throws CaseStudyException {
        Stock stock = stockRepository.findByName(stockDTO.getName()).orElseThrow(()
                -> new CaseStudyException(String.format("Stock with name %s not found", stockDTO.getName())));
        stock.setCurrentPrice(stockDTO.getCurrentPrice());
        return StockMapper.INSTANCE.toDTO(stockRepository.save(stock));
    }

    public void deleteStock(String name) throws CaseStudyException {
        Stock stock = stockRepository.findByName(name).orElseThrow(() -> new CaseStudyException(String.format("Stock with name %s not found", name)));

        Set<StockExchange> stockExchanges = stockExchangeRepository.findByStocksContains(stock);
        for (StockExchange stockExchange : stockExchanges) {
            stockExchange.getStocks().remove(stock);
            stockExchangeRepository.save(stockExchange);
        }
        stockRepository.delete(stock);
    }
}