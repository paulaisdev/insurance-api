package com.insurance.api.service;

import com.insurance.api.messaging.CsvConsumer;
import com.insurance.api.model.Apolice;
import com.insurance.api.repository.ApoliceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvImportService {

    private static final Logger logger = LoggerFactory.getLogger(CsvConsumer.class);

    @Autowired
    private ApoliceRepository apoliceRepository;

    public void processarCsv(MultipartFile file) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String linha;
            List<Apolice> apolices = new ArrayList<>();
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                Apolice apolice = new Apolice();
                apolice.setDescricao(dados[0]);
                apolice.setCpf(dados[1]);
                apolice.setSituacao(dados[2]);
                apolice.setPremioTotal(new BigDecimal(dados[3]));
                apolices.add(apolice);
            }
            apoliceRepository.saveAll(apolices);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar CSV", e);
        }
    }

    public void processarLinhaCsv(String linha) {
        try {
            String[] dados = linha.split(",");

            if (dados.length < 4) {
                logger.warn("Linha inválida no CSV: {}", linha);
                return;
            }

            Apolice apolice = new Apolice();
            apolice.setDescricao(dados[0]);
            apolice.setCpf(dados[1]);
            apolice.setSituacao(dados[2]);
            apolice.setPremioTotal(new BigDecimal(dados[3]));

            apoliceRepository.save(apolice);
            logger.info("Apólice cadastrada com sucesso: {}", apolice);
        } catch (Exception e) {
            logger.error("Erro ao processar linha CSV: {}", linha, e);
        }
    }
}