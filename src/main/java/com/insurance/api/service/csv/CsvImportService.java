package com.insurance.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class CsvImportService implements CsvService {

    private static final Logger logger = LoggerFactory.getLogger(CsvImportService.class);

    private final LinhaCsvProcessor linhaCsvProcessor;

    @Autowired
    public CsvImportService(LinhaCsvProcessor linhaCsvProcessor) {
        this.linhaCsvProcessor = linhaCsvProcessor;
    }

    @Override
    public void processarCsv(MultipartFile file) {
        Optional.ofNullable(file)
                .filter(f -> !f.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("O arquivo CSV está nulo ou vazio."));

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String linha = br.readLine(); // Ler e ignorar o cabeçalho
            while ((linha = br.readLine()) != null) {
                linhaCsvProcessor.processarLinhaCsv(linha);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao processar CSV", e);
        }
    }
}
