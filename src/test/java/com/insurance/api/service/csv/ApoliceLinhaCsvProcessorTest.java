package com.insurance.api.service.csv;

import com.insurance.api.model.Apolice;
import com.insurance.api.repository.ApoliceRepository;
import com.opencsv.exceptions.CsvException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApoliceLinhaCsvProcessorTest {

    @Mock
    private ApoliceRepository apoliceRepository;

    @Mock
    private PlatformTransactionManager transactionManager;

    @Mock
    private TransactionStatus transactionStatus;

    @InjectMocks
    private ApoliceLinhaCsvProcessor apoliceLinhaCsvProcessor;

    @Test
    void deveProcessarLinhaCsvComSucesso() throws IOException, CsvException {
        String linha = "1,Seguro Vida,12345678901,ATIVA,1000.00";

        when(transactionManager.getTransaction(any())).thenReturn(transactionStatus);

        assertDoesNotThrow(() -> apoliceLinhaCsvProcessor.processarLinhaCsv(linha));
        verify(apoliceRepository, times(1)).save(any(Apolice.class));
        verify(transactionManager, times(1)).commit(transactionStatus);
    }

    @Test
    void deveLancarErroParaLinhaInvalida() {
        String linha = "1,Seguro Auto";

        when(transactionManager.getTransaction(any())).thenReturn(transactionStatus);

        Exception exception = assertThrows(ValidationException.class, () -> apoliceLinhaCsvProcessor.processarLinhaCsv(linha));
        assertTrue(exception.getMessage().contains("Linha CSV não contém os campos esperados"));
        verify(apoliceRepository, never()).save(any(Apolice.class));
        verify(transactionManager, times(1)).rollback(transactionStatus);
    }

    @Test
    void deveLancarErroQuandoCpfForInvalido() {
        String linha = "1,Seguro Auto,123,ATIVA,2000.50";

        when(transactionManager.getTransaction(any())).thenReturn(transactionStatus);

        Exception exception = assertThrows(ValidationException.class, () -> apoliceLinhaCsvProcessor.processarLinhaCsv(linha));
        assertTrue(exception.getMessage().contains("CPF inválido"));
        verify(apoliceRepository, never()).save(any(Apolice.class));
        verify(transactionManager, times(1)).rollback(transactionStatus);
    }

    @Test
    void deveLancarErroQuandoPremioForInvalido() {
        String linha = "1,Seguro Residencial,98765432100,ATIVA,abc";

        when(transactionManager.getTransaction(any())).thenReturn(transactionStatus);

        Exception exception = assertThrows(ValidationException.class, () -> apoliceLinhaCsvProcessor.processarLinhaCsv(linha));
        assertTrue(exception.getMessage().contains("Erro ao converter premioTotal"));
        verify(apoliceRepository, never()).save(any(Apolice.class));
        verify(transactionManager, times(1)).rollback(transactionStatus);
    }

    @Test
    void deveLancarErroParaLinhaCsvVazia() {
        String linha = "";

        when(transactionManager.getTransaction(any())).thenReturn(transactionStatus);

        Exception exception = assertThrows(ValidationException.class, () -> apoliceLinhaCsvProcessor.processarLinhaCsv(linha));
        assertTrue(exception.getMessage().contains("Linha CSV vazia ou mal formatada"));
        verify(apoliceRepository, never()).save(any(Apolice.class));
        verify(transactionManager, times(1)).rollback(transactionStatus);
    }

    @Test
    void deveLancarErroQuandoSituacaoForInvalida() {
        String linha = "1,Seguro Residencial,98765432100,INDEFINIDA,1800.75";

        when(transactionManager.getTransaction(any())).thenReturn(transactionStatus);

        Exception exception = assertThrows(ValidationException.class, () -> apoliceLinhaCsvProcessor.processarLinhaCsv(linha));
        assertTrue(exception.getMessage().contains("Situação inválida"));
        verify(apoliceRepository, never()).save(any(Apolice.class));
        verify(transactionManager, times(1)).rollback(transactionStatus);
    }
}
