package com.ing.hubs.case_study.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.hubs.case_study.dto.StockAdditionDTO;
import com.ing.hubs.case_study.dto.StockDTO;
import com.ing.hubs.case_study.dto.StockExchangeCreationDTO;
import com.ing.hubs.case_study.dto.StockExchangeDTO;
import com.ing.hubs.case_study.service.StockExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StockExchangeController.class)
@ExtendWith(MockitoExtension.class)
public class StockExchangeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockExchangeService stockExchangeService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new StockExchangeController(stockExchangeService)).build();
    }

    @Test
    void testGetStockExchange() throws Exception {
        StockDTO stockDTO = StockDTO.builder()
                .id(1L)
                .name("AAPL")
                .description("Apple Inc.")
                .currentPrice(BigDecimal.valueOf(150))
                .build();
        Set<StockDTO> stocks = new HashSet<>();
        stocks.add(stockDTO);

        StockExchangeDTO stockExchangeDTO = StockExchangeDTO.builder()
                .id(1L)
                .name("NASDAQ")
                .description("NASDAQ Stock Exchange")
                .liveInMarket(true)
                .stocks(stocks)
                .build();

        when(stockExchangeService.getStockExchangeByName(anyString())).thenReturn(stockExchangeDTO);

        mockMvc.perform(get("/api/v1/stock-exchange/{name}", "NASDAQ")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("NASDAQ"))
                .andExpect(jsonPath("$.description").value("NASDAQ Stock Exchange"))
                .andExpect(jsonPath("$.liveInMarket").value(true))
                .andExpect(jsonPath("$.stocks[0].id").value(1))
                .andExpect(jsonPath("$.stocks[0].name").value("AAPL"))
                .andExpect(jsonPath("$.stocks[0].description").value("Apple Inc."))
                .andExpect(jsonPath("$.stocks[0].currentPrice").value(150));
    }

    @Test
    void testCreateStockExchange() throws Exception {
        StockExchangeCreationDTO stockExchangeCreationDTO = StockExchangeCreationDTO.builder()
                .name("NYSE")
                .description("New York Stock Exchange")
                .build();

        StockExchangeDTO createdStockExchange = StockExchangeDTO.builder()
                .id(2L)
                .name("NYSE")
                .description("New York Stock Exchange")
                .liveInMarket(true)
                .stocks(new HashSet<>())
                .build();

        when(stockExchangeService.createStockExchange(any(StockExchangeCreationDTO.class))).thenReturn(createdStockExchange);

        mockMvc.perform(post("/api/v1/stock-exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockExchangeCreationDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("NYSE"))
                .andExpect(jsonPath("$.description").value("New York Stock Exchange"))
                .andExpect(jsonPath("$.liveInMarket").value(true));
    }

    @Test
    void testAddStockToExchange() throws Exception {
        StockAdditionDTO stockAdditionDTO = StockAdditionDTO.builder()
                .name("GOOGL")
                .build();

        doNothing().when(stockExchangeService).addStockToExchange(anyString(), any(StockAdditionDTO.class));

        mockMvc.perform(post("/api/v1/stock-exchange/{name}", "NASDAQ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockAdditionDTO)))
                .andExpect(status().isOk());

        verify(stockExchangeService, times(1)).addStockToExchange("NASDAQ", stockAdditionDTO);
    }

    @Test
    void testDeleteStockFromExchange() throws Exception {
        doNothing().when(stockExchangeService).deleteStockFromExchange(anyString(), anyString());

        mockMvc.perform(delete("/api/v1/stock-exchange/{exchangeName}/{stockName}", "NYSE", "AAPL"))
                .andExpect(status().isOk());

        verify(stockExchangeService, times(1)).deleteStockFromExchange("NYSE", "AAPL");
    }
}