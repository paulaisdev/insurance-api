package com.insurance.api.mapper;

import com.insurance.api.dto.ApoliceDTO;
import com.insurance.api.model.Apolice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = ParcelaMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApoliceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "dataAlteracao", ignore = true)
    @Mapping(target = "usuarioCriacao", ignore = true)
    @Mapping(target = "usuarioAlteracao", ignore = true)
    @Mapping(target = "parcelas", source = "parcelas")

    Apolice toEntity(ApoliceDTO dto);

    ApoliceDTO toDTO(Apolice entity);
}
