package com.insurance.api.service.csv;

import com.insurance.api.model.Parcela;
import com.insurance.api.utils.CsvParser;
import com.insurance.api.utils.validator.ApoliceValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ParcelaCsvParser implements CsvParser<Parcela> {

    @Override
    public List<Parcela> parse(String[] dados) {
        List<Parcela> parcelas = new ArrayList<>();

        for (int i = 5; i < dados.length; i += 3) {
            if (i + 2 >= dados.length) {
                break;
            }

            Parcela parcela = new Parcela();
            parcela.setPremio(ApoliceValidator.converterPremio(dados[i]));
            parcela.setDataPagamento(LocalDate.parse(dados[i + 1].trim()));
            parcela.setFormaPagamento(dados[i + 2].trim());

            parcelas.add(parcela);
        }
        return parcelas;
    }
}
