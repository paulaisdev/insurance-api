package com.insurance.api.payment.impl;

import com.insurance.api.model.Parcela;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class JurosBoletoTest {

    private final JurosBoleto jurosBoleto = new JurosBoleto();

    @Test
    void deveCalcularJurosParaParcelaAtrasada() {
        Parcela parcela = new Parcela();
        parcela.setDataPagamento(LocalDate.now().minusDays(5));
        parcela.setPremio(new BigDecimal("1000"));

        BigDecimal juros = jurosBoleto.calcularJuros(parcela);

        assertEquals(new BigDecimal("150.00"), juros);
    }

    @Test
    void deveRetornarZeroParaParcelaSemAtraso() {
        Parcela parcela = new Parcela();
        parcela.setDataPagamento(LocalDate.now());
        parcela.setPremio(new BigDecimal("1000"));

        BigDecimal juros = jurosBoleto.calcularJuros(parcela);

        assertEquals(BigDecimal.ZERO, juros);
    }
}
