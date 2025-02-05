package com.insurance.api.messaging;

import com.insurance.api.service.CsvImportService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CsvConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CsvConsumer.class);
    private final CsvImportService csvImportService; // Troca de ApoliceService para CsvImportService

    public CsvConsumer(CsvImportService csvImportService) {
        this.csvImportService = csvImportService;
    }

    @KafkaListener(topics = "csv_apolices", groupId = "insurance_group")
    public void consumirMensagem(ConsumerRecord<String, String> record) {
        String transactionId = record.key();
        String linhaCsv = record.value();

        logger.info("Processando apólice. TransactionID: {}", transactionId);

        try {
            csvImportService.processarLinhaCsv(linhaCsv); // Agora chamando o método correto
        } catch (Exception e) {
            logger.error("Erro ao processar apólice. TransactionID: {}, Erro: {}", transactionId, e.getMessage());
        }
    }
}