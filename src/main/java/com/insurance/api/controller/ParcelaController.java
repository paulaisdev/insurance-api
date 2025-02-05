package com.insurance.api.controller;

import com.insurance.api.dto.ApiResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/parcelas")
public class ParcelaController {

    private static final Logger logger = LoggerFactory.getLogger(ParcelaController.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ParcelaController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/{id}/processar-pagamento")
    public ResponseEntity<ApiResponseDTO<?>> enviarPagamento(@PathVariable Long id, @RequestParam String formaPagamento) {
        String transactionId = UUID.randomUUID().toString();

        if (formaPagamento == null || formaPagamento.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>(HttpStatus.BAD_REQUEST,"Forma de pagamento é obrigatória.", null, transactionId));
        }

        logger.info("Iniciando processamento de pagamento. TransactionID: {}, ParcelaID: {}, Forma de Pagamento: {}",
                transactionId, id, formaPagamento);

        try {
            CompletableFuture<Void> future = kafkaTemplate.send("pagamento_parcela", String.valueOf(id), formaPagamento)
                    .thenAccept(result -> logger.info("Pagamento enviado para processamento. TransactionID: {}, ParcelaID: {}",
                            transactionId, id));

            future.join();

            return ResponseEntity.accepted().body(new ApiResponseDTO<>(HttpStatus.OK,"Pagamento enviado para processamento.", null, transactionId));
        } catch (Exception e) {
            logger.error("Erro ao enviar pagamento para Kafka. TransactionID: {}, Erro: {}", transactionId, e.getMessage());
            return ResponseEntity.internalServerError().body(new ApiResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR,"Erro ao processar pagamento.", null, transactionId));
        }
    }
}
