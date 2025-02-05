package com.insurance.api.service;

import com.insurance.api.dto.ApoliceDTO;
import com.insurance.api.mapper.ApoliceMapper;
import com.insurance.api.model.Apolice;
import com.insurance.api.model.Parcela;
import com.insurance.api.repository.ApoliceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ApoliceService {

    private static final Logger logger = LoggerFactory.getLogger(ApoliceService.class);
    private final ApoliceRepository apoliceRepository;
    private final ApoliceMapper apoliceMapper;
    private final CsvProcessorService csvProcessorService;

    public ApoliceService(ApoliceRepository apoliceRepository,
                          ApoliceMapper apoliceMapper,
                          CsvProcessorService csvProcessorService) {
        this.apoliceRepository = apoliceRepository;
        this.apoliceMapper = apoliceMapper;
        this.csvProcessorService = csvProcessorService;
    }

    public List<ApoliceDTO> listarTodas() {
        List<Apolice> apolices = apoliceRepository.findAll();
        return apolices.stream().map(apoliceMapper::toDTO).toList();
    }

    public Optional<ApoliceDTO> buscarPorId(Long id) {
        return apoliceRepository.findById(id).map(apoliceMapper::toDTO);
    }

    public ApoliceDTO salvar(ApoliceDTO apoliceDTO) {
        Apolice apolice = apoliceMapper.toEntity(apoliceDTO);

        Optional.ofNullable(apolice.getParcelas())
                .ifPresent(parcelas -> parcelas.forEach(parcela -> setarDadosPadraoParcela(parcela, apolice)));

        Apolice saved = apoliceRepository.save(apolice);
        logger.info("Apólice salva com ID: {}", saved.getId());
        return apoliceMapper.toDTO(saved);
    }

    private void setarDadosPadraoParcela(Parcela parcela, Apolice apolice) {
        parcela.setApolice(apolice);

        if (isSituacaoVazia(parcela)) {
            parcela.setSituacao("PENDENTE");
        }
    }

    private boolean isSituacaoVazia(Parcela parcela) {
        return parcela.getSituacao() == null || parcela.getSituacao().trim().isEmpty();
    }

    public void processarUploadCsv(MultipartFile file) {
        logger.info("Recebendo arquivo CSV: {}", file.getOriginalFilename());

        if (file.isEmpty()) {
            logger.warn("Arquivo CSV vazio recebido.");
            throw new IllegalArgumentException("O arquivo CSV está vazio.");
        }

        csvProcessorService.processarCsv(file);
    }
}
