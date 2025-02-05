package com.insurance.api.service;

import com.insurance.api.model.Parcela;
import com.insurance.api.payment.CalculoJuros;
import com.insurance.api.payment.impl.JurosCartao;
import com.insurance.api.payment.impl.JurosBoleto;
import com.insurance.api.payment.impl.JurosDinheiro;
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
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Parcela parcela;

    @BeforeEach
    void setUp() {
        Map<String, CalculoJuros> estrategiasPagamento = Map.of(
                "CARTAO", new JurosCartao(),
                "BOLETO", new JurosBoleto(),
                "DINHEIRO", new JurosDinheiro()
        );

        pagamentoService = new PagamentoService(parcelaRepository, estrategiasPagamento, kafkaTemplate);

        parcela = new Parcela();
        parcela.setId(1L);
        parcela.setPremio(new BigDecimal("1000.00"));
        parcela.setDataPagamento(LocalDate.now().minusDays(2));
        parcela.setSituacao("PENDENTE");
    }

    @Test
    void deveProcessarPagamentoComJuros() {
        when(parcelaRepository.findById(1L)).thenReturn(Optional.of(parcela));
        when(parcelaRepository.save(any())).thenReturn(parcela);

        Parcela resultado = pagamentoService.processarPagamento(1L, "CARTAO");

        assertNotNull(resultado);
        assertEquals("PAGO", resultado.getSituacao());
        assertTrue(resultado.getJuros().compareTo(BigDecimal.ZERO) > 0);

        verify(parcelaRepository, times(1)).save(parcela);
    }

    @Test
    void deveLancarExcecaoSeParcelaNaoForEncontrada() {
        when(parcelaRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                pagamentoService.processarPagamento(1L, "CARTAO"));

        assertEquals("Parcela não encontrada", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoSeParcelaJaForPaga() {
        parcela.setSituacao("PAGO");
        when(parcelaRepository.findById(1L)).thenReturn(Optional.of(parcela));

        Exception exception = assertThrows(RuntimeException.class, () ->
                pagamentoService.processarPagamento(1L, "CARTAO"));

        assertEquals("Parcela já foi paga ou está inválida", exception.getMessage());
    }
}
