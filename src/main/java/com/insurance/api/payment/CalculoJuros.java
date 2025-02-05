package com.insurance.api.payment;

import com.insurance.api.model.Parcela;
import java.math.BigDecimal;

public interface CalculoJuros {
    BigDecimal calcularJuros(Parcela parcela);
}
