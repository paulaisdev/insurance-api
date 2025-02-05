package com.insurance.api.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ParcelaDTO {

    @NotNull(message = "Prêmio é obrigatório")
    @Positive(message = "Prêmio deve ser positivo")
    private BigDecimal premio;

    @Pattern(regexp = "DINHEIRO|CARTAO|BOLETO", message = "Forma de pagamento deve ser DINHEIRO, CARTAO ou BOLETO")
    private String formaPagamento;

    @NotNull(message = "Data de pagamento é obrigatória")
    private LocalDate dataPagamento;
}
