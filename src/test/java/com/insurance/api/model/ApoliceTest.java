package com.insurance.api.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApoliceTest {

    @Test
    void deveCriarApoliceComConstrutor() {
        Apolice apolice = new Apolice("Seguro Vida", "12345678901", "ATIVA", new BigDecimal("2000.00"));

        assertNotNull(apolice);
        assertEquals("Seguro Vida", apolice.getDescricao());
        assertEquals("12345678901", apolice.getCpf());
        assertEquals("ATIVA", apolice.getSituacao());
        assertEquals(new BigDecimal("2000.00"), apolice.getPremioTotal());
        assertNotNull(apolice.getDataCriacao());
    }

    @Test
    void deveDefinirEObterValoresDeApolice() {
        Apolice apolice = new Apolice();
        apolice.setId(1L);
        apolice.setDescricao("Seguro Auto");
        apolice.setCpf("98765432100");
        apolice.setSituacao("INATIVA");
        apolice.setPremioTotal(new BigDecimal("3000.00"));
        apolice.setDataCriacao(LocalDate.of(2024, 1, 1));
        apolice.setUsuarioCriacao(10);
        apolice.setParcelas(List.of(new Parcela()));

        assertEquals(1L, apolice.getId());
        assertEquals("Seguro Auto", apolice.getDescricao());
        assertEquals("98765432100", apolice.getCpf());
        assertEquals("INATIVA", apolice.getSituacao());
        assertEquals(new BigDecimal("3000.00"), apolice.getPremioTotal());
        assertEquals(LocalDate.of(2024, 1, 1), apolice.getDataCriacao());
        assertEquals(10, apolice.getUsuarioCriacao());
        assertEquals(1, apolice.getParcelas().size());
    }
}
