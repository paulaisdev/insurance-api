package com.insurance.api.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CsvProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private CsvProducer csvProducer;

    @Test
    void deveEnviarMensagemParaOTopicoKafka() {
        String mensagem = "Teste de mensagem";
        String transactionId = "TX-12345";

        csvProducer.enviarMensagem(mensagem, transactionId);

        verify(kafkaTemplate, times(1)).send("csv_apolices", transactionId, mensagem);
    }
}
