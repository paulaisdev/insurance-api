package com.insurance.api.dto;

import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ParcelaDTOTest {

    private final Validator validator;

    public ParcelaDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void deveValidarParcelaComDadosCorretos() {
        ParcelaDTO parcelaDTO = new ParcelaDTO();
        parcelaDTO.setPremio(new BigDecimal("500.00"));
        parcelaDTO.setFormaPagamento("CARTAO");
        parcelaDTO.setDataPagamento(LocalDate.now());

        Set<ConstraintViolation<ParcelaDTO>> violations = validator.validate(parcelaDTO);

        assertTrue(violations.isEmpty());
    }

    @Test
    void deveFalharQuandoPremioForNegativo() {
        ParcelaDTO parcelaDTO = new ParcelaDTO();
        parcelaDTO.setPremio(new BigDecimal("-500.00"));
        parcelaDTO.setFormaPagamento("CARTAO");
        parcelaDTO.setDataPagamento(LocalDate.now());

        Set<ConstraintViolation<ParcelaDTO>> violations = validator.validate(parcelaDTO);

        assertFalse(violations.isEmpty());
        assertEquals("Prêmio deve ser positivo", violations.iterator().next().getMessage());
    }

    @Test
    void deveFalharQuandoFormaPagamentoForInvalida() {
        ParcelaDTO parcelaDTO = new ParcelaDTO();
        parcelaDTO.setPremio(new BigDecimal("500.00"));
        parcelaDTO.setFormaPagamento("PIX");
        parcelaDTO.setDataPagamento(LocalDate.now());

        Set<ConstraintViolation<ParcelaDTO>> violations = validator.validate(parcelaDTO);

        assertFalse(violations.isEmpty());
        assertEquals("Forma de pagamento deve ser DINHEIRO, CARTAO ou BOLETO", violations.iterator().next().getMessage());
    }

    @Test
    void deveFalharQuandoDataPagamentoForNula() {
        ParcelaDTO parcelaDTO = new ParcelaDTO();
        parcelaDTO.setPremio(new BigDecimal("500.00"));
        parcelaDTO.setFormaPagamento("BOLETO");
        parcelaDTO.setDataPagamento(null);

        Set<ConstraintViolation<ParcelaDTO>> violations = validator.validate(parcelaDTO);

        assertFalse(violations.isEmpty());
        assertEquals("Data de pagamento é obrigatória", violations.iterator().next().getMessage());
    }
}
