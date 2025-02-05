package com.insurance.api.utils.validator;

import com.insurance.api.model.Parcela;
import jakarta.validation.ValidationException;

public class ParcelaValidator {
    public static void validarParcela(Parcela parcela) {
        if (parcela.getApolice() == null) {
            throw new ValidationException("Parcela não vinculada a uma apólice.");
        }
        if (!"PENDENTE".equalsIgnoreCase(parcela.getSituacao())) {
            throw new ValidationException("Parcela já foi paga ou está inválida.");
        }
    }
}
