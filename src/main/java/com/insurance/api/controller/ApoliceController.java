package com.insurance.api.controller;

import com.insurance.api.dto.ApoliceDTO;
import com.insurance.api.service.ApoliceService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/apolices")
public class ApoliceController {

    private static final Logger logger = LoggerFactory.getLogger(ApoliceController.class);
    private final ApoliceService apoliceService;

    public ApoliceController(ApoliceService apoliceService) {
        this.apoliceService = apoliceService;
    }

    @GetMapping
    public ResponseEntity<List<ApoliceDTO>> listarTodas() {
        String transactionId = UUID.randomUUID().toString();
        logger.info("Listando todas as apólices. TransactionID: {}", transactionId);

        List<ApoliceDTO> apolices = apoliceService.listarTodas();
        if (apolices.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(apolices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApoliceDTO> buscarPorId(@PathVariable Long id) {
        String transactionId = UUID.randomUUID().toString();
        logger.info("Buscando apólice por ID. TransactionID: {}, ApoliceID: {}", transactionId, id);

        Optional<ApoliceDTO> apolice = apoliceService.buscarPorId(id);
        return apolice
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Apólice não encontrada. TransactionID: {}", transactionId);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                });
    }

    @PostMapping
    public ResponseEntity<ApoliceDTO> criar(@RequestBody @Valid ApoliceDTO apoliceDTO) {
        String transactionId = UUID.randomUUID().toString();
        logger.info("Criando nova apólice. TransactionID: {}", transactionId);

        try {
            ApoliceDTO novaApolice = apoliceService.salvar(apoliceDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaApolice);
        } catch (IllegalArgumentException e) {
            logger.warn("Erro de validação ao criar apólice. TransactionID: {}", transactionId);
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Erro interno ao criar apólice. TransactionID: {}", transactionId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
