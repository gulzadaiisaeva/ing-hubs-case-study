package com.ing.hubs.case_study.mapper;


import com.ing.hubs.case_study.dto.StockCreationDTO;
import com.ing.hubs.case_study.dto.StockDTO;
import com.ing.hubs.case_study.model.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StockMapper {
    StockMapper INSTANCE = Mappers.getMapper(StockMapper.class);

    StockDTO toDTO(Stock stock);
    Stock toEntity(StockCreationDTO stockCreationDTO);
}