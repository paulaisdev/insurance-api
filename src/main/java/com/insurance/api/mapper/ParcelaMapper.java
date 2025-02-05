package com.insurance.api.mapper;

import com.insurance.api.dto.ParcelaDTO;
import com.insurance.api.model.Parcela;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParcelaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", expression = "java(java.time.LocalDate.now())")
    Parcela toEntity(ParcelaDTO dto);

    ParcelaDTO toDTO(Parcela entity);
}
