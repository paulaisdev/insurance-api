package com.insurance.api.service.csv;

import com.insurance.api.model.Apolice;
import com.insurance.api.utils.CsvParser;
import com.insurance.api.utils.validator.ApoliceValidator;
import jakarta.validation.ValidationException;

import java.util.List;

public class ApoliceCsvParser implements CsvParser<Apolice> {

    @Override
    public List<Apolice> parse(String[] dados) {
        if (dados.length < 5) {
            throw new ValidationException("Linha CSV não contém os campos esperados.");
        }

        Apolice apolice = new Apolice();
        apolice.setDescricao(dados[1].trim());

        String cpf = dados[2].trim();
        ApoliceValidator.validarCpf(cpf);
        apolice.setCpf(cpf);

        String situacao = dados[3].trim().toUpperCase();
        ApoliceValidator.validarSituacao(situacao);
        apolice.setSituacao(situacao);

        apolice.setPremioTotal(ApoliceValidator.converterPremio(dados[4]));

        return List.of(apolice);
    }
}
