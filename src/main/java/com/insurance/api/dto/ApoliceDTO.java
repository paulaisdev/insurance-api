package com.insurance.api.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ApoliceDTO {

    private Long id;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    private String cpf;

    @NotBlank(message = "Situação é obrigatória")
    private String situacao;

    @NotNull(message = "Prêmio total é obrigatório")
    @Positive(message = "Prêmio total deve ser positivo")
    private BigDecimal premioTotal;

    private LocalDate dataCriacao;

    private LocalDate dataAlteracao;

    private Integer usuarioCriacao;

    private Integer usuarioAlteracao;

    private List<ParcelaDTO> parcelas;
}
