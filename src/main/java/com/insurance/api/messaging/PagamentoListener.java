package com.insurance.api.messaging;

import com.insurance.api.service.PagamentoService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PagamentoListener {

    @Autowired
    private PagamentoService pagamentoService;

    @KafkaListener(topics = "pagamento_parcela", groupId = "insurance_group")
    public void processarPagamento(ConsumerRecord<String, String> record) {
        try {
            Long parcelaId = Long.parseLong(record.key());
            String formaPagamento = record.value();

            if (formaPagamento == null || formaPagamento.isEmpty()) {
                formaPagamento = "CARTAO";
            }

            pagamentoService.processarPagamento(parcelaId, formaPagamento);
            System.out.println("Pagamento processado para parcela ID: " + parcelaId + " com forma de pagamento: " + formaPagamento);
        } catch (NumberFormatException e) {
            System.err.println("Erro: ID inválido no Kafka: " + record.key());
        } catch (RuntimeException e) {
            System.err.println("Erro no processamento do pagamento: " + e.getMessage());
        }
    }
}
