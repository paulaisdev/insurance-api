package com.insurance.api.messaging;

import com.insurance.api.service.csv.CsvImportService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CsvConsumerTest {

    @Mock
    private CsvImportService csvImportService;

    @InjectMocks
    private CsvConsumer csvConsumer;

    @BeforeEach
    void setUp() {
        Mockito.clearInvocations(csvImportService);
    }

    @Test
    void deveConsumirMensagemCsvComSucesso() {
        String mensagemValida = "dados,de,teste";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("csv_apolices", 0, 0L, "TX123", mensagemValida);

        assertDoesNotThrow(() -> csvConsumer.consumirMensagem(record));

        verify(csvImportService, times(1)).processarLinhaCsv(mensagemValida);
    }

    @Test
    void deveIgnorarMensagemNula() {
        assertDoesNotThrow(() -> csvConsumer.consumirMensagem(null));

        verify(csvImportService, never()).processarLinhaCsv(any());
    }

    @Test
    void deveIgnorarMensagemVazia() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("csv_apolices", 0, 0L, "TX123", "");

        assertDoesNotThrow(() -> csvConsumer.consumirMensagem(record));

        verify(csvImportService, never()).processarLinhaCsv(any());
    }
}
