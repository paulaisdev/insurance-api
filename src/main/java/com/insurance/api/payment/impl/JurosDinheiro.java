package com.insurance.api.payment.impl;

import com.insurance.api.model.Parcela;
import com.insurance.api.payment.CalculoJuros;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component("DINHEIRO")
public class JurosDinheiro implements CalculoJuros {

    private static final Logger logger = LoggerFactory.getLogger(JurosDinheiro.class);

    @Override
    public BigDecimal calcularJuros(Parcela parcela) {
        long diasAtraso = ChronoUnit.DAYS.between(parcela.getDataPagamento(), LocalDate.now());

        BigDecimal juros = diasAtraso > 0
                ? parcela.getPremio().multiply(BigDecimal.valueOf(0.03 * diasAtraso))
                .setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        logger.info("Cálculo de juros para DINHEIRO - Parcela ID: {}, Dias em atraso: {}, Juros calculado: {}",
                parcela.getId(), diasAtraso, juros);

        return juros;
    }
}
