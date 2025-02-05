package com.insurance.api.service;

import com.insurance.api.model.Parcela;
import com.insurance.api.payment.CalculoJuros;
import com.insurance.api.repository.ParcelaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
public class PagamentoService {

    private static final Logger logger = LoggerFactory.getLogger(PagamentoService.class);
    private final ParcelaRepository parcelaRepository;
    private final Map<String, CalculoJuros> estrategiasPagamento;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public PagamentoService(ParcelaRepository parcelaRepository, Map<String, CalculoJuros> estrategiasPagamento, KafkaTemplate<String, String> kafkaTemplate) {
        this.parcelaRepository = parcelaRepository;
        this.estrategiasPagamento = estrategiasPagamento;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Parcela processarPagamento(Long parcelaId, String formaPagamento) {
        String transactionId = "TX-" + parcelaId; // ID para rastreamento da transação
        logger.info("[{}] Iniciando pagamento para a parcela ID: {}, Forma de pagamento: {}", transactionId, parcelaId, formaPagamento);

        Parcela parcela = parcelaRepository.findById(parcelaId)
                .orElseThrow(() -> {
                    logger.error("[{}] Parcela não encontrada para o ID: {}", transactionId, parcelaId);
                    return new RuntimeException("Parcela não encontrada");
                });

        if (!"PENDENTE".equalsIgnoreCase(parcela.getSituacao())) {
            logger.warn("[{}] Parcela já foi paga ou está inválida. ID: {}, Situação atual: {}", transactionId, parcelaId, parcela.getSituacao());
            throw new RuntimeException("Parcela já foi paga ou está inválida");
        }

        CalculoJuros calculoJuros = estrategiasPagamento.get(formaPagamento.toUpperCase());
        if (calculoJuros == null) {
            logger.error("[{}] Forma de pagamento inválida recebida: {}", transactionId, formaPagamento);
            throw new RuntimeException("Forma de pagamento inválida");
        }

        BigDecimal juros = calculoJuros.calcularJuros(parcela);
        parcela.setJuros(juros);
        parcela.setFormaPagamento(formaPagamento);
        parcela.setDataPago(LocalDate.now());
        parcela.setSituacao("PAGO");

        String mensagem = String.format("Pagamento da parcela %d concluído via %s", parcelaId, formaPagamento);
        kafkaTemplate.send("pagamento_concluido", mensagem);

        parcela = parcelaRepository.save(parcela);
        logger.info("[{}] Pagamento concluído. Parcela ID: {}, Forma: {}, Juros aplicados: R${}", transactionId, parcelaId, formaPagamento, juros);

        return parcela;
    }
}
