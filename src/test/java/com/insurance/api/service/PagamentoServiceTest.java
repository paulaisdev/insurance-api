package com.insurance.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.api.model.Parcela;
import com.insurance.api.payment.CalculoJuros;
import com.insurance.api.repository.ParcelaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Mock
    private ParcelaRepository parcelaRepository;

    @Mock
    private Map<String, CalculoJuros> estrategiasPagamento;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Parcela parcela;

    @BeforeEach
    void setUp() {
        parcela = new Parcela();
        parcela.setId(1L);
        parcela.setSituacao("PENDENTE");
        parcela.setFormaPagamento("CARTAO");
        parcela.setDataPagamento(LocalDate.now().minusDays(3));
    }

    @Test
    void deveProcessarPagamentoComSucesso() throws Exception {
        when(parcelaRepository.findById(1L)).thenReturn(Optional.of(parcela));
        when(estrategiasPagamento.get("CARTAO")).thenReturn(p -> new BigDecimal("30.00"));
        when(parcelaRepository.save(any())).thenReturn(parcela);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"parcelaId\":1,\"formaPagamento\":\"CARTAO\"}");

        Parcela resultado = pagamentoService.processarPagamento(1L, "CARTAO");

        assertEquals("PAGO", resultado.getSituacao());

        verify(kafkaTemplate, times(1)).send(eq("pagamento_concluido"), anyString());
    }


    @Test
    void deveLancarErroAoPagarParcelaInexistente() {
        when(parcelaRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> pagamentoService.processarPagamento(99L, "CARTAO"));
        assertEquals("Parcela não encontrada", exception.getMessage());
    }

    @Test
    void deveLancarErroAoPagarParcelaJaPaga() {
        parcela.setSituacao("PAGO");
        when(parcelaRepository.findById(1L)).thenReturn(Optional.of(parcela));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> pagamentoService.processarPagamento(1L, "CARTAO"));
        assertEquals("Parcela já foi paga ou está inválida", exception.getMessage());
    }
}
