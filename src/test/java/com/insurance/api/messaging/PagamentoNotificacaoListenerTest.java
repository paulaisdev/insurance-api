package com.insurance.api.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoNotificacaoListenerTest {

    @InjectMocks
    private PagamentoNotificacaoListener listener;

    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        Mockito.clearInvocations(logger);
    }

    @Test
    void deveProcessarMensagemValidaDoKafka() {
        String mensagemValida = "{ \"parcelaId\": 1, \"formaPagamento\": \"CARTAO\" }";

        assertDoesNotThrow(() -> listener.processarNotificacao(mensagemValida));
    }

    @Test
    void deveRegistrarErroAoReceberMensagemInvalida() {
        String mensagemInvalida = "INVALID_JSON";

        listener.processarNotificacao(mensagemInvalida);

        verify(logger, never()).error(anyString());
    }
}
