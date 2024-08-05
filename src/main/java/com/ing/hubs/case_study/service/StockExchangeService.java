package com.ing.hubs.case_study.service;

import com.ing.hubs.case_study.constant.ApplicationConstants;
import com.ing.hubs.case_study.dto.StockAdditionDTO;
import com.ing.hubs.case_study.dto.StockExchangeCreationDTO;
import com.ing.hubs.case_study.dto.StockExchangeDTO;
import com.ing.hubs.case_study.exception.CaseStudyException;
import com.ing.hubs.case_study.mapper.StockExchangeMapper;
import com.ing.hubs.case_study.model.Stock;
import com.ing.hubs.case_study.model.StockExchange;
import com.ing.hubs.case_study.repository.StockExchangeRepository;
import com.ing.hubs.case_study.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockExchangeService {

    private final StockExchangeRepository stockExchangeRepository;
    private final StockRepository stockRepository;

    @Transactional(readOnly = true)
    public StockExchangeDTO getStockExchangeByName(String name) throws CaseStudyException {
        String decodedName = URLDecoder.decode(name, StandardCharsets.UTF_8);
        Optional<StockExchange> stockExchange = stockExchangeRepository.findByName(decodedName);
        if (stockExchange.isPresent()) {
            if(!stockExchange.get().isLiveInMarket()){
                throw new CaseStudyException(String.format("Stock exchange with name:%s is not live in the market", name));
            }
            return StockExchangeMapper.INSTANCE.toDTO(stockExchange.get());
        } else {
            throw new CaseStudyException(String.format("Stock exchange with name:%s not found", name));
        }
    }

    @Transactional
    public StockExchangeDTO createStockExchange(StockExchangeCreationDTO stockExchangeDTO) throws CaseStudyException {
        Optional<StockExchange> existingExchange = stockExchangeRepository.findByName(stockExchangeDTO.getName());
        if (existingExchange.isPresent()) {
            throw new CaseStudyException(String.format("StockExchange with name %s already exists", stockExchangeDTO.getName()));
        }
        StockExchange stockExchange = StockExchangeMapper.INSTANCE.toEntity(stockExchangeDTO);
        return StockExchangeMapper.INSTANCE.toDTO(stockExchangeRepository.save(stockExchange));
    }

    @Transactional
    public void addStockToExchange(String name, StockAdditionDTO stockDTO) throws CaseStudyException {
        String decodedName = URLDecoder.decode(name, StandardCharsets.UTF_8);
        StockExchange stockExchange = stockExchangeRepository.findByName(decodedName)
                .orElseThrow(() -> new CaseStudyException(String.format("Stock exchange with name:%s not found", name)));

        Optional<Stock> existingStock = stockRepository.findByName(stockDTO.getName());
        if (existingStock.isEmpty()) {
            throw new CaseStudyException(String.format("Stock with name:%s not found", stockDTO.getName()));
        }
        if (stockExchange.getStocks().contains(existingStock.get())) {
            throw new CaseStudyException(String.format("Stock with name %s already exists in this Stock Exchange", name));
        }
        stockExchange.getStocks().add(existingStock.get());
        if (stockExchange.getStocks().size() >= 5) {
            stockExchange.setLiveInMarket(true);
        }

        updateLiveInMarket(stockExchange);
        stockExchangeRepository.save(stockExchange);


    }

    @Transactional
    public void deleteStockFromExchange(String name, String stockName) throws CaseStudyException {
        String decodedName = URLDecoder.decode(name, StandardCharsets.UTF_8);
        StockExchange stockExchange = stockExchangeRepository.findByName(decodedName)
                .orElseThrow(() -> new CaseStudyException(String.format("Stock exchange with name:%s not found", name)));
        Stock stock = stockRepository.findByName(stockName)
                .orElseThrow(() -> new CaseStudyException(String.format("Stock with name:%s not found", stockName)));
        if (stockExchange.getStocks().contains(stock)) {
            stockExchange.getStocks().remove(stock);
        }else {
            throw new CaseStudyException(String.format("Stock with name %s is not exists in this Stock Exchange", name));
        }

        updateLiveInMarket(stockExchange);
        stockExchangeRepository.save(stockExchange);
    }

    private void updateLiveInMarket(StockExchange stockExchange) {
        boolean liveInMarket = stockExchange.getStocks().size() >= ApplicationConstants.LIVE_IN_MARKET_LIMIT;
        stockExchange.setLiveInMarket(liveInMarket);
    }

}