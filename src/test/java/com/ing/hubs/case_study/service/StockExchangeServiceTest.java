package com.ing.hubs.case_study.service;

import com.ing.hubs.case_study.dto.StockAdditionDTO;
import com.ing.hubs.case_study.dto.StockDTO;
import com.ing.hubs.case_study.dto.StockExchangeCreationDTO;
import com.ing.hubs.case_study.dto.StockExchangeDTO;
import com.ing.hubs.case_study.exception.CaseStudyException;
import com.ing.hubs.case_study.mapper.StockExchangeMapper;
import com.ing.hubs.case_study.model.Stock;
import com.ing.hubs.case_study.model.StockExchange;
import com.ing.hubs.case_study.repository.StockExchangeRepository;
import com.ing.hubs.case_study.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class StockExchangeServiceTest {

    @InjectMocks
    private StockExchangeService stockExchangeService;
    @Mock
    private StockExchangeRepository stockExchangeRepository;
    @Mock
    private StockExchangeMapper StockExchangeMapper;
    @Mock
    private StockRepository stockRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testGetStockExchangeByName_Success_StockExchangeLive() throws Exception {
        String name = "NYSE";
        StockExchange stockExchange = new StockExchange();
        stockExchange.setName(name);
        stockExchange.setLiveInMarket(true);
        stockExchange.setStocks(Set.of(new Stock()));

        StockExchangeDTO stockExchangeDTO = StockExchangeDTO.builder()
                .name(name)
                .liveInMarket(true)
                .stocks(Set.of(StockDTO.builder().build()))
                .build();

        when(stockExchangeRepository.findByName(anyString())).thenReturn(Optional.of(stockExchange));
        when(StockExchangeMapper.toDTO(stockExchange)).thenReturn(stockExchangeDTO);

        StockExchangeDTO result = stockExchangeService.getStockExchangeByName(name);

        assertNotNull(result);
        assertTrue(result.isLiveInMarket());
        verify(stockExchangeRepository).findByName(name);
    }

    @Test
    void testGetStockExchangeByName_Failure_NotLiveInMarket() throws Exception {
        String name = "NYSE";
        StockExchange stockExchange = new StockExchange();
        stockExchange.setName(name);
        stockExchange.setLiveInMarket(false);
        stockExchange.setStocks(Set.of(new Stock()));

        when(stockExchangeRepository.findByName(anyString())).thenReturn(Optional.of(stockExchange));

        CaseStudyException exception = assertThrows(CaseStudyException.class, () ->
                stockExchangeService.getStockExchangeByName(name));
        assertEquals(String.format("Stock exchange with name:%s is not live in the market", name), exception.getMessage());
        verify(stockExchangeRepository).findByName(name);
    }

    @Test
    void testCreateStockExchange_Failure_AlreadyExists() throws CaseStudyException {
        StockExchangeCreationDTO dto = StockExchangeCreationDTO.builder()
                .name("NASDAQ")
                .build();

        StockExchange existingExchange = new StockExchange();
        existingExchange.setName("NASDAQ");

        when(stockExchangeRepository.findByName(dto.getName())).thenReturn(Optional.of(existingExchange));

        CaseStudyException exception = assertThrows(CaseStudyException.class, () ->
                stockExchangeService.createStockExchange(dto));
        assertEquals(String.format("StockExchange with name %s already exists", dto.getName()), exception.getMessage());
        verify(stockExchangeRepository).findByName(dto.getName());
        verifyNoMoreInteractions(stockExchangeRepository);

    }

   @Test
    void testAddStockToExchange_Success() throws CaseStudyException {
        String exchangeName = "NYSE";
        StockAdditionDTO stockAdditionDTO = StockAdditionDTO.builder().name("AAPL").build();

        StockExchange stockExchange = new StockExchange();
        stockExchange.setName(exchangeName);
        stockExchange.setStocks(new HashSet<>());

        Stock stock = new Stock();
        stock.setName("AAPL");

        when(stockExchangeRepository.findByName(exchangeName)).thenReturn(Optional.of(stockExchange));
        when(stockRepository.findByName(stockAdditionDTO.getName())).thenReturn(Optional.of(stock));

        stockExchangeService.addStockToExchange(exchangeName, stockAdditionDTO);

        assertTrue(stockExchange.getStocks().contains(stock));
        verify(stockExchangeRepository).findByName(exchangeName);
        verify(stockRepository).findByName(stockAdditionDTO.getName());
        verify(stockExchangeRepository).save(stockExchange);
    }

    @Test
    void testAddStockToExchange_Failure_StockNotFound() throws CaseStudyException {
        String exchangeName = "NYSE";
        StockAdditionDTO stockAdditionDTO = StockAdditionDTO.builder().name("AAPL").build();

        when(stockExchangeRepository.findByName(exchangeName)).thenReturn(Optional.of(new StockExchange()));
        when(stockRepository.findByName(stockAdditionDTO.getName())).thenReturn(Optional.empty());

        CaseStudyException exception = assertThrows(CaseStudyException.class, () ->
                stockExchangeService.addStockToExchange(exchangeName, stockAdditionDTO));
        assertEquals(String.format("Stock with name:%s not found", stockAdditionDTO.getName()), exception.getMessage());
        verify(stockExchangeRepository).findByName(exchangeName);
        verify(stockRepository).findByName(stockAdditionDTO.getName());
        verifyNoMoreInteractions(stockExchangeRepository);
    }
    @Test
    void testDeleteStockFromExchange_Success() throws CaseStudyException {
        String exchangeName = "NYSE";
        String stockName = "AAPL";

        StockExchange stockExchange = new StockExchange();
        stockExchange.setName(exchangeName);
        stockExchange.setStocks(new HashSet<>());

        Stock stock = new Stock();
        stock.setName(stockName);

        stockExchange.getStocks().add(stock);

        when(stockExchangeRepository.findByName(exchangeName)).thenReturn(Optional.of(stockExchange));
        when(stockRepository.findByName(stockName)).thenReturn(Optional.of(stock));

        stockExchangeService.deleteStockFromExchange(exchangeName, stockName);

        assertFalse(stockExchange.getStocks().contains(stock));
        verify(stockExchangeRepository).findByName(exchangeName);
        verify(stockRepository).findByName(stockName);
        verify(stockExchangeRepository).save(stockExchange);
    }

    @Test
    void testDeleteStockFromExchange_Failure_StockNotFound() throws CaseStudyException {
        String exchangeName = "NYSE";
        String stockName = "AAPL";

        StockExchange stockExchange = new StockExchange();
        stockExchange.setName(exchangeName);

        when(stockExchangeRepository.findByName(exchangeName)).thenReturn(Optional.of(stockExchange));
        when(stockRepository.findByName(stockName)).thenReturn(Optional.empty());

        CaseStudyException exception = assertThrows(CaseStudyException.class, () ->
                stockExchangeService.deleteStockFromExchange(exchangeName, stockName));
        assertEquals(String.format("Stock with name:%s not found", stockName), exception.getMessage());
        verify(stockExchangeRepository).findByName(exchangeName);
        verify(stockRepository).findByName(stockName);
        verifyNoMoreInteractions(stockExchangeRepository);
    }




}
