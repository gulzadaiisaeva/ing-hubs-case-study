package com.ing.hubs.case_study.mapper;


import com.ing.hubs.case_study.dto.StockExchangeCreationDTO;
import com.ing.hubs.case_study.dto.StockExchangeDTO;
import com.ing.hubs.case_study.model.StockExchange;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StockExchangeMapper {
    StockExchangeMapper INSTANCE = Mappers.getMapper(StockExchangeMapper.class);

    StockExchangeDTO toDTO(StockExchange stockExchange);
    StockExchange toEntity(StockExchangeCreationDTO stockExchangeDTO);
}