package com.insurance.api.service;

import com.insurance.api.model.Parcela;
import com.insurance.api.payment.CalculoJuros;
import com.insurance.api.repository.ParcelaRepository;
import com.insurance.api.utils.validator.ParcelaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
public class PagamentoService {

    private static final Logger logger = LoggerFactory.getLogger(PagamentoService.class);
    private final ParcelaRepository parcelaRepository;
    private final Map<String, CalculoJuros> estrategiasPagamento;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PagamentoService(ParcelaRepository parcelaRepository, Map<String, CalculoJuros> estrategiasPagamento,
                            KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.parcelaRepository = parcelaRepository;
        this.estrategiasPagamento = estrategiasPagamento;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public Parcela processarPagamento(Long parcelaId, String formaPagamento) {
        String transactionId = "TX-" + parcelaId;
        logger.info("[{}] Iniciando pagamento para a parcela ID: {}, Forma de pagamento: {}", transactionId, parcelaId, formaPagamento);

        Parcela parcela = parcelaRepository.findById(parcelaId)
                .orElseThrow(() -> {
                    logger.error("[{}] Parcela não encontrada para o ID: {}", transactionId, parcelaId);
                    return new IllegalArgumentException("Parcela não encontrada");
                });

        ParcelaValidator.validarParcela(parcela);

        CalculoJuros calculoJuros = estrategiasPagamento.get(formaPagamento.toUpperCase());
        if (calculoJuros == null) {
            logger.error("[{}] Forma de pagamento inválida recebida: {}", transactionId, formaPagamento);
            throw new IllegalArgumentException("Forma de pagamento inválida");
        }

        BigDecimal juros = calculoJuros.calcularJuros(parcela);
        parcela.setJuros(juros);
        parcela.setFormaPagamento(formaPagamento);
        parcela.setDataPago(LocalDate.now());
        parcela.setSituacao("PAGO");

        try {
            String mensagemKafka = objectMapper.writeValueAsString(Map.of(
                    "parcelaId", parcelaId,
                    "formaPagamento", formaPagamento,
                    "jurosAplicados", juros,
                    "dataPagamento", parcela.getDataPago()
            ));

            if (mensagemKafka == null || mensagemKafka.isBlank()) {
                throw new IllegalArgumentException("Mensagem não pode ser nula ao enviar para o Kafka.");
            }

            kafkaTemplate.send("pagamento_concluido", mensagemKafka);
        } catch (Exception e) {
            logger.error("[{}] Erro ao enviar mensagem Kafka: {}", transactionId, e.getMessage());
        }

        parcela = parcelaRepository.save(parcela);
        logger.info("[{}] Pagamento concluído. Parcela ID: {}, Forma: {}, Juros aplicados: R${}", transactionId, parcelaId, formaPagamento, juros);

        return parcela;
    }
}
