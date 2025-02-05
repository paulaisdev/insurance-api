package com.insurance.api.service;

import com.insurance.api.dto.ApoliceDTO;
import com.insurance.api.mapper.ApoliceMapper;
import com.insurance.api.model.Apolice;
import com.insurance.api.repository.ApoliceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        apoliceDTO = new ApoliceDTO();
        apoliceDTO.setId(1L);
        apoliceDTO.setDescricao("Apólice Teste");
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

        Optional<ApoliceDTO> resultado = apoliceService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Apólice Teste", resultado.get().getDescricao());
        verify(apoliceRepository, times(1)).findById(1L);
    }

    @Test
    void deveSalvarApolice() {
        when(apoliceMapper.toEntity(any())).thenReturn(apolice);
        when(apoliceRepository.save(any())).thenReturn(apolice);
        when(apoliceMapper.toDTO(any())).thenReturn(apoliceDTO);

        ApoliceDTO resultado = apoliceService.salvar(apoliceDTO);

        assertNotNull(resultado);
        assertEquals("Apólice Teste", resultado.getDescricao());
        verify(apoliceRepository, times(1)).save(apolice);
    }
}
