package com.insurance.api.controller;

import com.insurance.api.dto.ApoliceDTO;
import com.insurance.api.dto.ApiResponseDTO;
import com.insurance.api.service.ApoliceService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/apolices")
public class ApoliceController {

    private static final Logger logger = LoggerFactory.getLogger(ApoliceController.class);
    private final ApoliceService apoliceService;

    public ApoliceController(ApoliceService apoliceService) {
        this.apoliceService = apoliceService;
    }

    @GetMapping({"/", "/{id}"})
    public ResponseEntity<ApiResponseDTO<?>> listarApolices(@PathVariable(required = false) Long id) {
        String transactionId = UUID.randomUUID().toString();

        if (id != null) {
            logger.info("Buscando apólice por ID. TransactionID: {}, ApoliceID: {}", transactionId, id);
            ApoliceDTO apolice = apoliceService.buscarPorId(id);
            return ResponseEntity.ok(new ApiResponseDTO<>(HttpStatus.OK, "Apólice encontrada.", apolice, transactionId));
        }

        logger.info("Listando todas as apólices. TransactionID: {}", transactionId);
        List<ApoliceDTO> apolices = apoliceService.listarTodas();
        if (apolices.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(new ApiResponseDTO<>(HttpStatus.OK,"Lista de apólices.", apolices, transactionId));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ApoliceDTO>> criar(@RequestBody @Valid ApoliceDTO apoliceDTO) {
        String transactionId = UUID.randomUUID().toString();
        logger.info("Criando nova apólice. TransactionID: {}", transactionId);
        ApoliceDTO novaApolice = apoliceService.salvar(apoliceDTO);
        return ResponseEntity.status(201).body(new ApiResponseDTO<>(HttpStatus.CREATED,"Apólice criada com sucesso.", novaApolice, transactionId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ApoliceDTO>> atualizar(@PathVariable Long id, @RequestBody @Valid ApoliceDTO apoliceDTO) {
        String transactionId = UUID.randomUUID().toString();
        logger.info("Atualizando apólice. TransactionID: {}, ApoliceID: {}", transactionId, id);
        ApoliceDTO apoliceAtualizada = apoliceService.atualizar(id, apoliceDTO);
        return ResponseEntity.ok(new ApiResponseDTO<>(HttpStatus.OK,"Apólice atualizada com sucesso.", apoliceAtualizada, transactionId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> deletar(@PathVariable Long id) {
        String transactionId = UUID.randomUUID().toString();
        logger.info("Deletando apólice. TransactionID: {}, ApoliceID: {}", transactionId, id);
        apoliceService.deletar(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(HttpStatus.OK,"Apólice deletada com sucesso.", null, transactionId));
    }
}