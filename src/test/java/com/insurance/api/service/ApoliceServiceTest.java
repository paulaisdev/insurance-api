package com.insurance.api.service;

import com.insurance.api.dto.ApoliceDTO;
import com.insurance.api.exception.NotFoundException;
import com.insurance.api.mapper.ApoliceMapper;
import com.insurance.api.model.Apolice;
import com.insurance.api.repository.ApoliceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApoliceServiceTest {

    @Mock
    private ApoliceRepository apoliceRepository;

    @Mock
    private ApoliceMapper apoliceMapper;

    @InjectMocks
    private ApoliceService apoliceService;

    private Apolice apolice;
    private ApoliceDTO apoliceDTO;

    @BeforeEach
    void setUp() {
        apolice = new Apolice();
        apolice.setId(1L);
        apolice.setDescricao("Apólice Teste");
        apolice.setParcelas(new ArrayList<>());

        apoliceDTO = new ApoliceDTO();
        apoliceDTO.setId(1L);
        apoliceDTO.setDescricao("Apólice Teste");
        apoliceDTO.setParcelas(new ArrayList<>());
    }


    @Test
    void deveListarTodasApolices() {
        when(apoliceRepository.findAll()).thenReturn(List.of(apolice));
        when(apoliceMapper.toDTO(any())).thenReturn(apoliceDTO);

        List<ApoliceDTO> apolices = apoliceService.listarTodas();

        assertNotNull(apolices);
        assertEquals(1, apolices.size());
        verify(apoliceRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarApolicePorId() {
        when(apoliceRepository.findById(1L)).thenReturn(Optional.of(apolice));
        when(apoliceMapper.toDTO(any())).thenReturn(apoliceDTO);

        ApoliceDTO resultado = apoliceService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Apólice Teste", resultado.getDescricao());
        verify(apoliceRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoAoBuscarApoliceInexistente() {
        when(apoliceRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> apoliceService.buscarPorId(99L));
        assertEquals("Apólice não encontrada com o ID: 99", exception.getMessage());
    }

    @Test
    void deveSalvarApolice() {
        apolice.setParcelas(new ArrayList<>());

        when(apoliceMapper.toEntity(any())).thenReturn(apolice);
        when(apoliceRepository.save(any())).thenReturn(apolice);
        when(apoliceMapper.toDTO(any())).thenReturn(apoliceDTO);

        ApoliceDTO resultado = apoliceService.salvar(apoliceDTO);

        assertNotNull(resultado);
        assertEquals("Apólice Teste", resultado.getDescricao());
        verify(apoliceRepository, times(1)).save(apolice);
    }


    @Test
    void deveAtualizarApolice() {
        // Preparação: inicializando as parcelas para evitar NPE
        Apolice apoliceComParcelas = new Apolice();
        apoliceComParcelas.setParcelas(new ArrayList<>());

        when(apoliceRepository.findById(1L)).thenReturn(Optional.of(apoliceComParcelas));
        when(apoliceMapper.toEntity(any())).thenReturn(apoliceComParcelas);
        when(apoliceRepository.save(any())).thenReturn(apoliceComParcelas);
        when(apoliceMapper.toDTO(any())).thenReturn(apoliceDTO);

        ApoliceDTO resultado = apoliceService.atualizar(1L, apoliceDTO);

        assertNotNull(resultado);
        verify(apoliceRepository, times(1)).save(apoliceComParcelas);
    }


    @Test
    void deveLancarErroAoAtualizarApoliceInexistente() {
        when(apoliceRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> apoliceService.atualizar(99L, apoliceDTO));
        assertEquals("Apólice não encontrada para atualização. ID: 99", exception.getMessage());
    }

    @Test
    void deveDeletarApolice() {
        when(apoliceRepository.findById(1L)).thenReturn(Optional.of(apolice));

        apoliceService.deletar(1L);

        verify(apoliceRepository, times(1)).delete(apolice);
    }

    @Test
    void deveLancarErroAoDeletarApoliceInexistente() {
        when(apoliceRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> apoliceService.deletar(99L));
        assertEquals("Apólice não encontrada para deleção. ID: 99", exception.getMessage());
    }
}
