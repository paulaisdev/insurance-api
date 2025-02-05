package com.insurance.api.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ParcelaTest {

    @Test
    void deveCriarParcelaEVerificarValores() {
        Parcela parcela = new Parcela();
        parcela.setId(1L);
        parcela.setPremio(new BigDecimal("1000.00"));
        parcela.setFormaPagamento("BOLETO");
        parcela.setDataPagamento(LocalDate.of(2024, 2, 15));
        parcela.setSituacao("PENDENTE");

        assertNotNull(parcela);
        assertEquals(1L, parcela.getId());
        assertEquals(new BigDecimal("1000.00"), parcela.getPremio());
        assertEquals("BOLETO", parcela.getFormaPagamento());
        assertEquals(LocalDate.of(2024, 2, 15), parcela.getDataPagamento());
        assertEquals("PENDENTE", parcela.getSituacao());
    }

    @Test
    void deveAlterarValoresDeUmaParcela() {
        Parcela parcela = new Parcela();
        parcela.setJuros(new BigDecimal("10.50"));
        parcela.setDataPago(LocalDate.of(2024, 2, 20));
        parcela.setDataCriacao(LocalDate.of(2024, 1, 1));
        parcela.setUsuarioCriacao(11);

        assertEquals(new BigDecimal("10.50"), parcela.getJuros());
        assertEquals(LocalDate.of(2024, 2, 20), parcela.getDataPago());
        assertEquals(LocalDate.of(2024, 1, 1), parcela.getDataCriacao());
        assertEquals(11, parcela.getUsuarioCriacao());
    }
}
