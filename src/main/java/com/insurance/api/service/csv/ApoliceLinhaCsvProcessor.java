package com.insurance.api.service.csv;

import com.insurance.api.model.Apolice;
import com.insurance.api.model.Parcela;
import com.insurance.api.repository.ApoliceRepository;
import jakarta.validation.ValidationException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

@Service
public class ApoliceLinhaCsvProcessor implements LinhaCsvProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ApoliceLinhaCsvProcessor.class);
    private final ApoliceRepository apoliceRepository;
    private final PlatformTransactionManager transactionManager;
    private final ApoliceCsvParser apoliceParser;
    private final ParcelaCsvParser parcelaParser;

    public ApoliceLinhaCsvProcessor(ApoliceRepository apoliceRepository, PlatformTransactionManager transactionManager) {
        this.apoliceRepository = apoliceRepository;
        this.transactionManager = transactionManager;
        this.apoliceParser = new ApoliceCsvParser();
        this.parcelaParser = new ParcelaCsvParser();
    }

    @Override
    public void processarLinhaCsv(String linha) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            logger.info("Recebendo linha CSV para processamento: {}", linha);

            CSVParser parser = new CSVParserBuilder().withSeparator(',').build();
            try (CSVReader reader = new CSVReaderBuilder(new StringReader(linha)).withCSVParser(parser).build()) {
                List<String[]> registros = reader.readAll();

                if (registros.isEmpty()) {
                    throw new ValidationException("Linha CSV vazia ou mal formatada.");
                }

                String[] dados = registros.get(0);

                Apolice apolice = apoliceParser.parse(dados).get(0);
                List<Parcela> parcelas = parcelaParser.parse(dados);
                parcelas.forEach(parcela -> parcela.setApolice(apolice));

                apolice.setParcelas(parcelas);

                Apolice salvo = apoliceRepository.save(apolice);
                logger.info("Apólice e parcelas salvas com sucesso! ID Apólice: {}, Parcelas: {}", salvo.getId(), salvo.getParcelas().size());

                transactionManager.commit(status);
                logger.info("Transação commitada com sucesso!");
            }
        } catch (IOException | CsvException e) {
            logger.error("Erro ao ler/parsing CSV: {}", linha, e);
            transactionManager.rollback(status);
            throw new RuntimeException("Erro ao processar CSV.");
        } catch (ValidationException e) {
            logger.error("Erro de validação ao processar linha CSV: {}", linha, e);
            transactionManager.rollback(status);
            throw e;
        } catch (RuntimeException e) {
            logger.error("Erro inesperado ao processar linha CSV: {}", linha, e);
            transactionManager.rollback(status);
            throw new RuntimeException("Erro inesperado ao processar CSV.");
        }
    }
}
