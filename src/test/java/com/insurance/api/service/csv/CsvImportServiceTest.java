package com.insurance.api.service.csv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CsvImportServiceTest {

    @Mock
    private LinhaCsvProcessor linhaCsvProcessor;

    @InjectMocks
    private CsvImportService csvImportService;

    private MultipartFile file;

    @BeforeEach
    void setUp() {
        file = mock(MultipartFile.class);
    }

    @Test
    void deveImportarCsvComSucesso() throws IOException {
        when(file.isEmpty()).thenReturn(false);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("cabecalho\ndado1,dado2,dado3,100.00".getBytes()));

        assertDoesNotThrow(() -> csvImportService.processarCsv(file));
        verify(linhaCsvProcessor, times(1)).processarLinhaCsv("dado1,dado2,dado3,100.00");
    }

    @Test
    void deveLancarErroAoImportarCsvNulo() {
        MultipartFile file = null;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> csvImportService.processarCsv(file));
        assertEquals("O arquivo CSV está nulo ou vazio.", exception.getMessage());
    }

    @Test
    void deveLancarErroAoImportarCsvVazio() {
        when(file.isEmpty()).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> csvImportService.processarCsv(file));
        assertEquals("O arquivo CSV está nulo ou vazio.", exception.getMessage());
    }
}
