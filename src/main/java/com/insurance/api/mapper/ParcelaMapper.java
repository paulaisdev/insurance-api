package com.insurance.api.mapper;

import com.insurance.api.dto.ParcelaDTO;
import com.insurance.api.model.Parcela;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParcelaMapper {

    @Mapping(target = "dataCriacao", expression = "java(java.time.LocalDate.now())")
    Parcela toEntity(ParcelaDTO dto);

    @Mapping(target = "dataCriacao", source = "dataCriacao")
    @Mapping(target = "dataAlteracao", source = "dataAlteracao")
    @Mapping(target = "usuarioCriacao", source = "usuarioCriacao")
    @Mapping(target = "usuarioAlteracao", source = "usuarioAlteracao")
    ParcelaDTO toDTO(Parcela entity);
}
