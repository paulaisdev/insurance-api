package com.insurance.api.payment;

import com.insurance.api.model.Parcela;
import com.insurance.api.payment.impl.JurosBoleto;
import com.insurance.api.payment.impl.JurosCartao;
import com.insurance.api.payment.impl.JurosDinheiro;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CalculoJurosTest {

    @Test
    void deveCalcularJurosCartaoCorretamente() {
        JurosCartao jurosCartao = new JurosCartao();
        Parcela parcela = new Parcela();
        parcela.setPremio(new BigDecimal("1000.00"));
        parcela.setDataPagamento(LocalDate.now().minusDays(5));

        BigDecimal juros = jurosCartao.calcularJuros(parcela);
        assertEquals(new BigDecimal("150.00").setScale(2, RoundingMode.HALF_UP), juros);
    }

    @Test
    void deveCalcularJurosBoletoCorretamente() {
        JurosBoleto jurosBoleto = new JurosBoleto();
        Parcela parcela = new Parcela();
        parcela.setPremio(new BigDecimal("1000.00"));
        parcela.setDataPagamento(LocalDate.now().minusDays(5));

        BigDecimal juros = jurosBoleto.calcularJuros(parcela);
        assertEquals(new BigDecimal("150.00").setScale(2, RoundingMode.HALF_UP), juros);
    }

    @Test
    void deveCalcularJurosDinheiroCorretamente() {
        JurosDinheiro jurosDinheiro = new JurosDinheiro();
        Parcela parcela = new Parcela();
        parcela.setPremio(new BigDecimal("1000.00"));
        parcela.setDataPagamento(LocalDate.now().minusDays(5));

        BigDecimal juros = jurosDinheiro.calcularJuros(parcela);
        assertEquals(new BigDecimal("150.00").setScale(2, RoundingMode.HALF_UP), juros);
    }

    @Test
    void naoDeveCalcularJurosParaPagamentoNoPrazo() {
        JurosCartao jurosCartao = new JurosCartao();
        Parcela parcela = new Parcela();
        parcela.setPremio(new BigDecimal("1000.00"));
        parcela.setDataPagamento(LocalDate.now());

        BigDecimal juros = jurosCartao.calcularJuros(parcela);
        assertEquals(BigDecimal.ZERO, juros);
    }
}
