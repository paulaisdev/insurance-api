package com.insurance.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/parcelas")
public class ParcelaController {

    private static final Logger logger = LoggerFactory.getLogger(ParcelaController.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ParcelaController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/{id}/processar-pagamento")
    public ResponseEntity<String> enviarPagamento(@PathVariable Long id, @RequestParam String formaPagamento) {
        String transactionId = UUID.randomUUID().toString();
        logger.info("Iniciando processamento de pagamento. TransactionID: {}, ParcelaID: {}, Forma de Pagamento: {}",
                transactionId, id, formaPagamento);

        if (formaPagamento == null || formaPagamento.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Forma de pagamento é obrigatória.");
        }

        try {
            kafkaTemplate.send("pagamento_parcela", String.valueOf(id), formaPagamento);
            logger.info("Pagamento enviado para processamento. TransactionID: {}, ParcelaID: {}", transactionId, id);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Pagamento enviado para processamento da parcela " + id + " com forma de pagamento " + formaPagamento);
        } catch (Exception e) {
            logger.error("Erro ao enviar pagamento para Kafka. TransactionID: {}, Erro: {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar pagamento.");
        }
    }
}
