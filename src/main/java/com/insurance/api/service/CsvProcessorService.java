package com.insurance.api.service;

import com.insurance.api.messaging.CsvProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class CsvProcessorService {

    private static final Logger logger = LoggerFactory.getLogger(CsvProcessorService.class);
    private final CsvProducer csvProducer;

    public CsvProcessorService(CsvProducer csvProducer) {
        this.csvProducer = csvProducer;
    }

    public void processarCsv(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String transactionId = UUID.randomUUID().toString();
            reader.lines().skip(1).forEach(linha -> csvProducer.enviarMensagem(linha, transactionId));

            logger.info("Arquivo CSV enviado para processamento assíncrono. TransactionID: {}", transactionId);

        } catch (Exception e) {
            logger.error("Erro ao processar CSV: {}", e.getMessage());
            throw new RuntimeException("Erro ao processar o arquivo CSV.");
        }
    }
}
