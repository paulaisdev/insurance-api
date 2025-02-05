package com.insurance.api.service;

import com.insurance.api.dto.ApoliceDTO;
import com.insurance.api.exception.NotFoundException;
import com.insurance.api.mapper.ApoliceMapper;
import com.insurance.api.model.Apolice;
import com.insurance.api.model.Parcela;
import com.insurance.api.repository.ApoliceRepository;
import com.insurance.api.repository.ParcelaRepository;
import com.insurance.api.service.csv.CsvProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Service
public class ApoliceService {

    private static final Logger logger = LoggerFactory.getLogger(ApoliceService.class);
    private final ApoliceRepository apoliceRepository;
    private final ParcelaRepository parcelaRepository;
    private final ApoliceMapper apoliceMapper;
    private final CsvProcessorService csvProcessorService;

    public ApoliceService(ApoliceRepository apoliceRepository,
                          ParcelaRepository parcelaRepository,
                          ApoliceMapper apoliceMapper,
                          CsvProcessorService csvProcessorService) {
        this.apoliceRepository = apoliceRepository;
        this.parcelaRepository = parcelaRepository;
        this.apoliceMapper = apoliceMapper;
        this.csvProcessorService = csvProcessorService;
    }

    public List<ApoliceDTO> listarTodas() {
        return apoliceRepository.findAll()
                .stream()
                .map(apoliceMapper::toDTO)
                .toList();
    }

    public ApoliceDTO buscarPorId(Long id) {
        return apoliceRepository.findById(id)
                .map(apoliceMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Apólice não encontrada com o ID: " + id));
    }

    @Transactional
    public ApoliceDTO salvar(ApoliceDTO apoliceDTO) {
        Apolice apolice = apoliceMapper.toEntity(apoliceDTO);

        if (apolice.getParcelas() == null) {
            apolice.setParcelas(new ArrayList<>());
        }

        apolice.setDataCriacao(LocalDate.now());
        apolice.setUsuarioCriacao(12);

        apolice.getParcelas().forEach(parcela -> configurarNovaParcela(parcela, apolice));

        Apolice saved = apoliceRepository.save(apolice);
        logger.info("Apólice salva com ID: {}", saved.getId());
        return apoliceMapper.toDTO(saved);
    }


    public ApoliceDTO atualizar(Long id, ApoliceDTO apoliceDTO) {

        Apolice apoliceExistente = apoliceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Apólice não encontrada para atualização. ID: " + id));

        Apolice apoliceAtualizada = apoliceMapper.toEntity(apoliceDTO);

        apoliceAtualizada.setId(apoliceExistente.getId());
        apoliceAtualizada.setDataCriacao(apoliceExistente.getDataCriacao());
        apoliceAtualizada.setUsuarioCriacao(apoliceExistente.getUsuarioCriacao());
        apoliceAtualizada.setDataAlteracao(LocalDate.now());
        apoliceAtualizada.setUsuarioAlteracao(13);

        if (apoliceAtualizada.getParcelas() != null) {
            for (Parcela parcela : apoliceAtualizada.getParcelas()) {
                parcela.setApolice(apoliceAtualizada);
                parcela.setDataAlteracao(LocalDate.now());
                parcela.setUsuarioAlteracao(13);
            }
        }

        Apolice saved = apoliceRepository.save(apoliceAtualizada);
        apoliceRepository.flush();
        logger.info("Apólice salva com sucesso: {}", saved);
        return apoliceMapper.toDTO(saved);
    }


    @Transactional
    public void deletar(Long id) {
        Apolice apolice = apoliceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Apólice não encontrada para deleção. ID: " + id));

        apoliceRepository.delete(apolice);
        logger.info("Apólice deletada com ID: {}", id);
    }

    private void configurarNovaParcela(Parcela parcela, Apolice apolice) {
        parcela.setApolice(apolice);
        parcela.setSituacao(Optional.ofNullable(parcela.getSituacao()).orElse("PENDENTE"));
        parcela.setDataCriacao(LocalDate.now());
        parcela.setUsuarioCriacao(12);
    }

    private void configurarParcelaAtualizada(Parcela parcela, Apolice apolice) {
        parcela.setApolice(apolice);
        parcela.setDataAlteracao(LocalDate.now());
        parcela.setUsuarioAlteracao(13);
    }

    private void copiarDadosPersistentes(Apolice original, Apolice atualizado) {
        atualizado.setId(original.getId());
        atualizado.setDataCriacao(original.getDataCriacao());
        atualizado.setUsuarioCriacao(original.getUsuarioCriacao());
        atualizado.setDataAlteracao(LocalDate.now());
        atualizado.setUsuarioAlteracao(13);
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
