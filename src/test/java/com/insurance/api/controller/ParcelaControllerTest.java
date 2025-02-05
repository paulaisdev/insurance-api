package com.insurance.api.controller;

import com.insurance.api.dto.ApiResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.concurrent.CompletableFuture;
import org.springframework.kafka.support.SendResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParcelaControllerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private ParcelaController parcelaController;

    @BeforeEach
    void setUp() {
        CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();
        future.complete(mock(SendResult.class));

        lenient().when(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .thenReturn(future);
    }

    @Test
    void deveEnviarPagamentoComSucesso() {
        ResponseEntity<ApiResponseDTO<?>> response = parcelaController.enviarPagamento(1L, "CARTAO");

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.ACCEPTED, response.getBody().getStatus());
        assertEquals("Pagamento enviado para processamento.", response.getBody().getMensagem());
        assertNotNull(response.getBody().getTransactionId());

        verify(kafkaTemplate, times(1)).send(eq("pagamento_parcela"), eq("1"), eq("CARTAO"));
    }

    @Test
    void deveRetornarErroSeFormaDePagamentoForNula() {
        ResponseEntity<ApiResponseDTO<?>> response = parcelaController.enviarPagamento(1L, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());
        assertEquals("Forma de pagamento é obrigatória.", response.getBody().getMensagem());

        verifyNoInteractions(kafkaTemplate);
    }

    @Test
    void deveRetornarErroSeKafkaFalhar() {
        when(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Erro Kafka")));

        ResponseEntity<ApiResponseDTO<?>> response = parcelaController.enviarPagamento(1L, "CARTAO");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getBody().getStatus());
        assertEquals("Erro ao processar pagamento.", response.getBody().getMensagem());
        assertNotNull(response.getBody().getTransactionId());

        verify(kafkaTemplate, times(1)).send(eq("pagamento_parcela"), eq("1"), eq("CARTAO"));
    }
}
