package com.insurance.api.controller;

import com.insurance.api.service.CsvProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/csv")
public class CsvController {

    private static final Logger logger = LoggerFactory.getLogger(CsvController.class);
    private final CsvProcessorService csvProcessorService;

    public CsvController(CsvProcessorService csvProcessorService) {
        this.csvProcessorService = csvProcessorService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        logger.info("Recebendo arquivo CSV para processamento.");

        if (file.isEmpty()) {
            logger.warn("Tentativa de upload com arquivo vazio.");
            return ResponseEntity.badRequest().body("O arquivo CSV não pode estar vazio.");
        }

        try {
            csvProcessorService.processarCsv(file);
            return ResponseEntity.accepted().body("Arquivo recebido e sendo processado.");
        } catch (Exception e) {
            logger.error("Erro ao processar CSV: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao processar o arquivo.");
        }
    }
}