package com.insurance.api.controller;

import com.insurance.api.dto.ApiResponseDTO;
import com.insurance.api.service.csv.CsvProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/csv")
public class CsvController {

    private static final Logger logger = LoggerFactory.getLogger(CsvController.class);
    private final CsvProcessorService csvProcessorService;

    public CsvController(CsvProcessorService csvProcessorService) {
        this.csvProcessorService = csvProcessorService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<?>> uploadCsv(@RequestParam("file") MultipartFile file) {
        String transactionId = UUID.randomUUID().toString();
        logger.info("Recebendo arquivo CSV para processamento. TransactionID: {}", transactionId);

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>(HttpStatus.BAD_REQUEST,"O arquivo CSV não pode estar vazio.", null, transactionId));
        }

        try {
            csvProcessorService.processarCsv(file);
            return ResponseEntity.accepted().body(new ApiResponseDTO<>(HttpStatus.OK,"Arquivo recebido e sendo processado.", null, transactionId));
        } catch (Exception e) {
            logger.error("Erro ao processar CSV. TransactionID: {}, Erro: {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR,"Erro interno ao processar o arquivo.", null,transactionId));
        }
    }
}
