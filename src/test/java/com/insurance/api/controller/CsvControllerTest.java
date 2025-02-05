package com.insurance.api.controller;

import com.insurance.api.dto.ApiResponseDTO;
import com.insurance.api.service.csv.CsvProcessorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CsvControllerTest {

    @Mock
    private CsvProcessorService csvProcessorService;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private CsvController csvController;

    @Test
    void deveProcessarUploadCsvComSucesso() {
        when(file.isEmpty()).thenReturn(false);

        ResponseEntity<ApiResponseDTO<?>> response = csvController.uploadCsv(file);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Arquivo recebido e sendo processado.", response.getBody().getMensagem());
        verify(csvProcessorService, times(1)).processarCsv(file);
    }

    @Test
    void deveRetornarErroAoEnviarArquivoVazio() {
        when(file.isEmpty()).thenReturn(true);

        ResponseEntity<ApiResponseDTO<?>> response = csvController.uploadCsv(file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("O arquivo CSV não pode estar vazio.", response.getBody().getMensagem());
        verify(csvProcessorService, never()).processarCsv(file);
    }

    @Test
    void deveRetornarErroInternoSeProcessamentoFalhar() {
        when(file.isEmpty()).thenReturn(false);
        doThrow(new RuntimeException("Erro ao processar")).when(csvProcessorService).processarCsv(file);

        ResponseEntity<ApiResponseDTO<?>> response = csvController.uploadCsv(file);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro interno ao processar o arquivo.", response.getBody().getMensagem());
        verify(csvProcessorService, times(1)).processarCsv(file);
    }
}
