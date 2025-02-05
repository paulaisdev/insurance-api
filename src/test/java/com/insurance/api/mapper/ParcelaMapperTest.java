package com.insurance.api.mapper;

import com.insurance.api.dto.ParcelaDTO;
import com.insurance.api.model.Parcela;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ParcelaMapperTest {

    private final ParcelaMapper parcelaMapper = Mappers.getMapper(ParcelaMapper.class);

    @Test
    void deveConverterDTOParaEntidade() {
        ParcelaDTO dto = new ParcelaDTO();
        dto.setPremio(new BigDecimal("1000.00"));
        dto.setFormaPagamento("CARTAO");
        dto.setDataPagamento(LocalDate.of(2024, 1, 10));

        Parcela parcela = parcelaMapper.toEntity(dto);

        assertNotNull(parcela);
        assertEquals(dto.getPremio(), parcela.getPremio());
        assertEquals(dto.getFormaPagamento(), parcela.getFormaPagamento());
        assertEquals(dto.getDataPagamento(), parcela.getDataPagamento());
        assertNotNull(parcela.getDataCriacao());
    }

    @Test
    void deveConverterEntidadeParaDTO() {
        Parcela parcela = new Parcela();
        parcela.setId(1L);
        parcela.setPremio(new BigDecimal("500.00"));
        parcela.setFormaPagamento("BOLETO");
        parcela.setDataPagamento(LocalDate.of(2024, 2, 15));
        parcela.setDataCriacao(LocalDate.of(2024, 1, 5));

        ParcelaDTO dto = parcelaMapper.toDTO(parcela);

        assertNotNull(dto);
        assertEquals(parcela.getPremio(), dto.getPremio());
        assertEquals(parcela.getFormaPagamento(), dto.getFormaPagamento());
        assertEquals(parcela.getDataPagamento(), dto.getDataPagamento());
        assertEquals(parcela.getDataCriacao(), dto.getDataCriacao());
    }
}
