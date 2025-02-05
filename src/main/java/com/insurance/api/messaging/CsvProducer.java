package com.insurance.api.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CsvProducer {

    private static final Logger logger = LoggerFactory.getLogger(CsvProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    public CsvProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarMensagem(String mensagem, String transactionId) {
        logger.info("Enviando mensagem ao Kafka. TransactionID: {}", transactionId);
        kafkaTemplate.send("csv_apolices", transactionId, mensagem);
    }
}
