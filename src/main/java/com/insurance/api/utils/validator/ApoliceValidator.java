package com.insurance.api.service.csv.validator;

import java.util.regex.Pattern;

public class ApoliceValidator {

    private static final Pattern CPF_PATTERN = Pattern.compile("\\d{11}"); // CPF deve ter 11 dígitos numéricos

    public static void validarCpf(String cpf) {
        if (!CPF_PATTERN.matcher(cpf).matches()) {
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }
    }

    public static void validarSituacao(String situacao) {
        if (!situacao.equalsIgnoreCase("ATIVA") && !situacao.equalsIgnoreCase("CANCELADA")) {
            throw new IllegalArgumentException("Situação inválida: " + situacao);
        }
    }

    public static void validarCamposObrigatorios(String[] dados) {
        if (dados.length < 5) {
            throw new IllegalArgumentException("Campos ausentes na linha CSV.");
        }
    }
}
