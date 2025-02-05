package com.insurance.api.utils.validator;

import jakarta.validation.ValidationException;
import java.math.BigDecimal;

public class ApoliceValidator {

    public static void validarCpf(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new ValidationException("CPF inválido: " + cpf);
        }
    }

    public static void validarSituacao(String situacao) {
        if (!"ATIVA".equals(situacao) && !"CANCELADA".equals(situacao)) {
            throw new ValidationException("Situação inválida: " + situacao);
        }
    }

    public static BigDecimal converterPremio(String premio) {
        try {
            return new BigDecimal(premio.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new ValidationException("Erro ao converter premioTotal: " + premio);
        }
    }
}
