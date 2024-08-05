package com.ing.hubs.case_study.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.hubs.case_study.dto.StockCreationDTO;
import com.ing.hubs.case_study.dto.StockDTO;
import com.ing.hubs.case_study.dto.StockUpdateDTO;
import com.ing.hubs.case_study.service.StockService;
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
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StockController.class)
@ExtendWith(MockitoExtension.class)
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockService stockService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new StockController(stockService)).build();
    }

    @Test
    void testGetAllStocks() throws Exception {
        StockDTO stockDTO = StockDTO.builder()
                .id(1L)
                .name("AAPL")
                .description("Apple Inc.")
                .currentPrice(BigDecimal.valueOf(150))
                .build();
        when(stockService.getAllStocks()).thenReturn(Collections.singletonList(stockDTO));

        mockMvc.perform(get("/api/v1/stock")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("AAPL"))
                .andExpect(jsonPath("$[0].description").value("Apple Inc."))
                .andExpect(jsonPath("$[0].currentPrice").value(150));
    }

    @Test
    void testCreateStock() throws Exception {
        StockCreationDTO stockCreationDTO = StockCreationDTO.builder()
                .name("GOOGL")
                .description("Google LLC")
                .currentPrice(BigDecimal.valueOf(2800))
                .build();

        StockDTO createdStock = StockDTO.builder()
                .id(2L)
                .name("GOOGL")
                .description("Google LLC")
                .currentPrice(BigDecimal.valueOf(2800))
                .build();

        when(stockService.createStock(any(StockCreationDTO.class))).thenReturn(createdStock);

        mockMvc.perform(post("/api/v1/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockCreationDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("GOOGL"))
                .andExpect(jsonPath("$.description").value("Google LLC"))
                .andExpect(jsonPath("$.currentPrice").value(2800));
    }

    @Test
    void testUpdateStock() throws Exception {
        StockDTO stockDTO = StockDTO.builder()
                .id(1L)
                .name("AAPL")
                .description("Apple Inc.")
                .currentPrice(BigDecimal.valueOf(155))
                .build();
        when(stockService.updateStock(any(StockUpdateDTO.class))).thenReturn(stockDTO);

        mockMvc.perform(put("/api/v1/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("AAPL"))
                .andExpect(jsonPath("$.description").value("Apple Inc."))
                .andExpect(jsonPath("$.currentPrice").value(155));
    }

    @Test
    void testDeleteStock() throws Exception {
        doNothing().when(stockService).deleteStock(anyString());

        mockMvc.perform(delete("/api/v1/stock/{name}", "AAPL"))
                .andExpect(status().isOk());

        verify(stockService, times(1)).deleteStock("AAPL");
    }

//    @Test
//    void testCreateStockValidationError() throws Exception {
//        StockCreationDTO stockCreationDTO = StockCreationDTO.builder().build();
//
//        mockMvc.perform(post("/api/v1/stock")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(stockCreationDTO)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors[0]").value("Name is required"));
//    }
}
