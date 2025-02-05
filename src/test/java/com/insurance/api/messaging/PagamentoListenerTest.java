package com.insurance.api.messaging;

import com.insurance.api.service.PagamentoService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoListenerTest {

    @Mock
    private PagamentoService pagamentoService;

    @InjectMocks
    private PagamentoListener pagamentoListener;

    @Test
    void deveProcessarPagamentoComSucesso() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("pagamento_parcela", 0, 0, "1", "CARTAO");

        pagamentoListener.processarPagamento(record);

        verify(pagamentoService, times(1)).processarPagamento(1L, "CARTAO");
    }
}
