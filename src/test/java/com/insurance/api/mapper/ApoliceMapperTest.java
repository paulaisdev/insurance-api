package com.insurance.api.mapper;

import com.insurance.api.dto.ApoliceDTO;
import com.insurance.api.dto.ParcelaDTO;
import com.insurance.api.model.Apolice;
import com.insurance.api.model.Parcela;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApoliceMapperTest {

    @Mock
    private ParcelaMapper parcelaMapper;

    @InjectMocks
    private ApoliceMapperImpl apoliceMapper;

    @Test
    void deveConverterDTOParaEntidade() {
        ApoliceDTO dto = new ApoliceDTO();
        dto.setId(1L);
        dto.setDescricao("Seguro Auto");
        dto.setParcelas(List.of(new ParcelaDTO()));

        when(parcelaMapper.toEntity(any())).thenReturn(new Parcela());

        Apolice apolice = apoliceMapper.toEntity(dto);

        assertNotNull(apolice);
        assertEquals(1, apolice.getParcelas().size());
    }

    @Test
    void deveConverterEntidadeParaDTO() {
        Apolice apolice = new Apolice();
        apolice.setId(1L);
        apolice.setDescricao("Seguro Vida");
        apolice.setCpf("98765432100");
        apolice.setSituacao("INATIVA");
        apolice.setPremioTotal(new BigDecimal("5000.00"));
        apolice.setDataCriacao(LocalDate.of(2024, 1, 1));
        apolice.setUsuarioCriacao(12);

        ApoliceDTO dto = apoliceMapper.toDTO(apolice);

        assertNotNull(dto);
        assertEquals(apolice.getId(), dto.getId());
        assertEquals(apolice.getDescricao(), dto.getDescricao());
        assertEquals(apolice.getCpf(), dto.getCpf());
        assertEquals(apolice.getSituacao(), dto.getSituacao());
        assertEquals(apolice.getPremioTotal(), dto.getPremioTotal());
        assertEquals(apolice.getDataCriacao(), dto.getDataCriacao());
        assertEquals(apolice.getUsuarioCriacao(), dto.getUsuarioCriacao());
    }
}
