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

class ApoliceDTOTest {

    private final Validator validator;

    public ApoliceDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void deveValidarApoliceComDadosCorretos() {
        ApoliceDTO apoliceDTO = new ApoliceDTO();
        apoliceDTO.setDescricao("Seguro Auto");
        apoliceDTO.setCpf("12345678901");
        apoliceDTO.setSituacao("ATIVA");
        apoliceDTO.setPremioTotal(new BigDecimal("1500.00"));

        Set<ConstraintViolation<ApoliceDTO>> violations = validator.validate(apoliceDTO);

        assertTrue(violations.isEmpty());
    }

    @Test
    void deveFalharQuandoDescricaoForVazia() {
        ApoliceDTO apoliceDTO = new ApoliceDTO();
        apoliceDTO.setDescricao("");
        apoliceDTO.setCpf("12345678901");
        apoliceDTO.setSituacao("ATIVA");
        apoliceDTO.setPremioTotal(new BigDecimal("1500.00"));

        Set<ConstraintViolation<ApoliceDTO>> violations = validator.validate(apoliceDTO);

        assertFalse(violations.isEmpty());
        assertEquals("Descrição é obrigatória", violations.iterator().next().getMessage());
    }

    @Test
    void deveFalharQuandoCpfInvalido() {
        ApoliceDTO apoliceDTO = new ApoliceDTO();
        apoliceDTO.setDescricao("Seguro Auto");
        apoliceDTO.setCpf("1234");
        apoliceDTO.setSituacao("ATIVA");
        apoliceDTO.setPremioTotal(new BigDecimal("1500.00"));

        Set<ConstraintViolation<ApoliceDTO>> violations = validator.validate(apoliceDTO);

        assertFalse(violations.isEmpty());
        assertEquals("CPF deve conter 11 dígitos", violations.iterator().next().getMessage());
    }

    @Test
    void deveFalharQuandoPremioTotalForNegativo() {
        ApoliceDTO apoliceDTO = new ApoliceDTO();
        apoliceDTO.setDescricao("Seguro Auto");
        apoliceDTO.setCpf("12345678901");
        apoliceDTO.setSituacao("ATIVA");
        apoliceDTO.setPremioTotal(new BigDecimal("-500.00"));

        Set<ConstraintViolation<ApoliceDTO>> violations = validator.validate(apoliceDTO);

        assertFalse(violations.isEmpty());
        assertEquals("Prêmio total deve ser positivo", violations.iterator().next().getMessage());
    }
}
