package com.insurance.api.service.csv;

import com.insurance.api.messaging.CsvProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CsvProcessorServiceTest {

    @Mock
    private CsvProducer csvProducer;

    @InjectMocks
    private CsvProcessorService csvProcessorService;

    @Test
    void deveProcessarCsvComSucesso() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getInputStream()).thenAnswer(invocation ->
                new ByteArrayInputStream("cabecalho\ndados".getBytes(StandardCharsets.UTF_8))
        );

        assertDoesNotThrow(() -> csvProcessorService.processarCsv(file));
        verify(csvProducer, times(1)).enviarMensagem(anyString(), anyString());
    }


    @Test
    void deveLancarExcecaoAoReceberCsvVazio() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> csvProcessorService.processarCsv(file));
        assertEquals("O arquivo CSV está vazio.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoAoReceberArquivoNulo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> csvProcessorService.processarCsv(null));
        assertEquals("O arquivo CSV está vazio.", exception.getMessage());
    }
}

