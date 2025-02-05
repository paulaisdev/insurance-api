package com.insurance.api.payment.impl;

import com.insurance.api.model.Parcela;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class JurosDinheiroTest {

    private final JurosDinheiro jurosDinheiro = new JurosDinheiro();

    @Test
    void deveCalcularJurosParaPagamentoEmDinheiroComAtraso() {
        Parcela parcela = new Parcela();
        parcela.setDataPagamento(LocalDate.now().minusDays(2));
        parcela.setPremio(new BigDecimal("500"));

        BigDecimal juros = jurosDinheiro.calcularJuros(parcela);

        assertEquals(new BigDecimal("30.00"), juros);
    }

    @Test
    void deveRetornarZeroParaPagamentoEmDinheiroSemAtraso() {
        Parcela parcela = new Parcela();
        parcela.setDataPagamento(LocalDate.now());
        parcela.setPremio(new BigDecimal("500"));

        BigDecimal juros = jurosDinheiro.calcularJuros(parcela);

        assertEquals(BigDecimal.ZERO, juros);
    }
}
