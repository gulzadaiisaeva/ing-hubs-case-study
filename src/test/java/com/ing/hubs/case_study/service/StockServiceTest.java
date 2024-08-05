package com.ing.hubs.case_study.service;

import com.ing.hubs.case_study.dto.StockCreationDTO;
import com.ing.hubs.case_study.dto.StockDTO;
import com.ing.hubs.case_study.dto.StockUpdateDTO;
import com.ing.hubs.case_study.exception.CaseStudyException;
import com.ing.hubs.case_study.mapper.StockMapper;
import com.ing.hubs.case_study.model.Stock;
import com.ing.hubs.case_study.model.StockExchange;
import com.ing.hubs.case_study.repository.StockExchangeRepository;
import com.ing.hubs.case_study.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class StockServiceTest {

    @InjectMocks
    private StockService stockService;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockExchangeRepository stockExchangeRepository;
    @Mock
    private StockMapper stockMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStocks() {
        Stock stock = new Stock();
        stock.setId(1L);
        stock.setName("AAPL");
        stock.setDescription("Apple Inc.");
        stock.setCurrentPrice(BigDecimal.valueOf(150));

        StockDTO stockDTO = StockDTO.builder()
                .id(1L)
                .name("AAPL")
                .description("Apple Inc.")
                .currentPrice(BigDecimal.valueOf(150))
                .build();

        when(stockRepository.findAll()).thenReturn(Collections.singletonList(stock));
        when(stockMapper.toDTO(stock)).thenReturn(stockDTO);

        List<StockDTO> result = stockService.getAllStocks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("AAPL", result.get(0).getName());
        verify(stockRepository, times(1)).findAll();
    }



    @Test
    void testCreateStockAlreadyExists() throws CaseStudyException {
        StockCreationDTO stockCreationDTO = StockCreationDTO.builder()
                .name("AAPL")
                .description("Apple Inc.")
                .currentPrice(BigDecimal.valueOf(150))
                .build();

        Stock existingStock = new Stock();
        existingStock.setName("AAPL");

        when(stockRepository.findByName(anyString())).thenReturn(Optional.of(existingStock));

        CaseStudyException exception = assertThrows(CaseStudyException.class, () -> {
            stockService.createStock(stockCreationDTO);
        });
        assertEquals("Stock with name AAPL already exists", exception.getMessage());
        verify(stockRepository, times(1)).findByName("AAPL");
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    void testUpdateStockSuccess() throws CaseStudyException {
        StockUpdateDTO stockDTO = StockUpdateDTO.builder()
                .name("AAPL")
                .updatedPrice(BigDecimal.valueOf(155))
                .build();

        Stock stock = new Stock();
        stock.setName("AAPL");
        stock.setCurrentPrice(BigDecimal.valueOf(150));

        StockDTO updatedStockDTO = StockDTO.builder()
                .name("AAPL")
                .currentPrice(BigDecimal.valueOf(155))
                .build();

        when(stockRepository.findByName(anyString())).thenReturn(Optional.of(stock));
        when(stockRepository.save(stock)).thenReturn(stock);
        when(stockMapper.toDTO(stock)).thenReturn(updatedStockDTO);

        StockDTO result = stockService.updateStock(stockDTO);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(155), result.getCurrentPrice());
        verify(stockRepository, times(1)).findByName("AAPL");
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    void testUpdateStockNotFound() throws CaseStudyException {
        StockUpdateDTO stockDTO = StockUpdateDTO.builder()
                .name("AAPL")
                .updatedPrice(BigDecimal.valueOf(155))
                .build();

        when(stockRepository.findByName(anyString())).thenReturn(Optional.empty());

        CaseStudyException exception = assertThrows(CaseStudyException.class, () -> {
            stockService.updateStock(stockDTO);
        });
        assertEquals("Stock with name AAPL not found", exception.getMessage());
        verify(stockRepository, times(1)).findByName("AAPL");
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    void testDeleteStockSuccess() throws CaseStudyException {
        Stock stock = new Stock();
        stock.setName("AAPL");

        StockExchange stockExchange = new StockExchange();
        stockExchange.setId(1L);
        stockExchange.setName("NYSE");
        stockExchange.setStocks(new HashSet<>(Collections.singletonList(stock)));

        when(stockRepository.findByName(anyString())).thenReturn(Optional.of(stock));
        when(stockExchangeRepository.findByStocksContains(any(Stock.class))).thenReturn(Collections.singleton(stockExchange));

        stockService.deleteStock("AAPL");

        verify(stockRepository, times(1)).findByName("AAPL");
        verify(stockExchangeRepository, times(1)).findByStocksContains(stock);
        verify(stockRepository, times(1)).delete(stock);
    }

    @Test
    void testDeleteStockNotFound() throws CaseStudyException {
        when(stockRepository.findByName(anyString())).thenReturn(Optional.empty());

        CaseStudyException exception = assertThrows(CaseStudyException.class, () -> {
            stockService.deleteStock("AAPL");
        });
        assertEquals("Stock with name AAPL not found", exception.getMessage());
        verify(stockRepository, times(1)).findByName("AAPL");
        verify(stockRepository, never()).delete(any(Stock.class));
    }
}
