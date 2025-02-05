package com.insurance.api.payment.impl;

import com.insurance.api.model.Parcela;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class JurosCartaoTest {

    private final JurosCartao jurosCartao = new JurosCartao();

    @Test
    void deveCalcularJurosParaCartaoComAtraso() {
        Parcela parcela = new Parcela();
        parcela.setDataPagamento(LocalDate.now().minusDays(3));
        parcela.setPremio(new BigDecimal("2000"));

        BigDecimal juros = jurosCartao.calcularJuros(parcela);

        assertEquals(new BigDecimal("180.00"), juros);
    }

    @Test
    void deveRetornarZeroParaCartaoSemAtraso() {
        Parcela parcela = new Parcela();
        parcela.setDataPagamento(LocalDate.now());
        parcela.setPremio(new BigDecimal("2000"));

        BigDecimal juros = jurosCartao.calcularJuros(parcela);

        assertEquals(BigDecimal.ZERO, juros);
    }
}
