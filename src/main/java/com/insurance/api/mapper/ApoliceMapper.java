package com.insurance.api.mapper;

import com.insurance.api.dto.ApoliceDTO;
import com.insurance.api.model.Apolice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = ParcelaMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApoliceMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "parcelas", source = "parcelas")
    Apolice toEntity(ApoliceDTO dto);

    @Mapping(target = "dataCriacao", source = "dataCriacao")
    @Mapping(target = "dataAlteracao", source = "dataAlteracao")
    @Mapping(target = "usuarioCriacao", source = "usuarioCriacao")
    @Mapping(target = "usuarioAlteracao", source = "usuarioAlteracao")
    ApoliceDTO toDTO(Apolice entity);
}
