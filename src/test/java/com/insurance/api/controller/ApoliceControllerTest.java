package com.insurance.api.controller;

import com.insurance.api.dto.ApoliceDTO;
import com.insurance.api.dto.ApiResponseDTO;
import com.insurance.api.service.ApoliceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApoliceControllerTest {

    @Mock
    private ApoliceService apoliceService;

    @InjectMocks
    private ApoliceController apoliceController;

    private ApoliceDTO apoliceDTO;

    @BeforeEach
    void setUp() {
        apoliceDTO = new ApoliceDTO();
        apoliceDTO.setId(1L);
        apoliceDTO.setDescricao("Apólice Teste");
    }

    @Test
    void deveListarTodasApolicesComSucesso() {
        when(apoliceService.listarTodas()).thenReturn(List.of(apoliceDTO));

        ResponseEntity<ApiResponseDTO<?>> response = apoliceController.listarApolices(null);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody().getDados());
        verify(apoliceService, times(1)).listarTodas();
    }

    @Test
    void deveRetornarNoContentQuandoNaoExistemApolices() {
        when(apoliceService.listarTodas()).thenReturn(List.of());

        ResponseEntity<ApiResponseDTO<?>> response = apoliceController.listarApolices(null);

        assertEquals(204, response.getStatusCodeValue());
        verify(apoliceService, times(1)).listarTodas();
    }

    @Test
    void deveBuscarApolicePorIdComSucesso() {
        when(apoliceService.buscarPorId(1L)).thenReturn(apoliceDTO);

        ResponseEntity<ApiResponseDTO<?>> response = apoliceController.listarApolices(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(apoliceDTO, response.getBody().getDados());
        verify(apoliceService, times(1)).buscarPorId(1L);
    }

    @Test
    void deveCriarApoliceComSucesso() {
        when(apoliceService.salvar(any())).thenReturn(apoliceDTO);

        ResponseEntity<ApiResponseDTO<ApoliceDTO>> response = apoliceController.criar(apoliceDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(apoliceDTO, response.getBody().getDados());
        verify(apoliceService, times(1)).salvar(apoliceDTO);
    }

    @Test
    void deveAtualizarApoliceComSucesso() {
        when(apoliceService.atualizar(eq(1L), any())).thenReturn(apoliceDTO);

        ResponseEntity<ApiResponseDTO<ApoliceDTO>> response = apoliceController.atualizar(1L, apoliceDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(apoliceDTO, response.getBody().getDados());
        verify(apoliceService, times(1)).atualizar(1L, apoliceDTO);
    }

    @Test
    void deveDeletarApoliceComSucesso() {
        doNothing().when(apoliceService).deletar(1L);

        ResponseEntity<ApiResponseDTO<?>> response = apoliceController.deletar(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Apólice deletada com sucesso.", response.getBody().getMensagem());
        verify(apoliceService, times(1)).deletar(1L);
    }
}
