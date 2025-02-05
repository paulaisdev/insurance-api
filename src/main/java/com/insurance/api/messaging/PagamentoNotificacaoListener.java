package com.insurance.api.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PagamentoNotificacaoListener {

    @KafkaListener(topics = "pagamento_concluido", groupId = "insurance_group")
    public void processarNotificacao(String mensagem) {
        System.out.println("Notificação recebida: " + mensagem);
    }
}
