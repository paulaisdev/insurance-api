package com.insurance.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO<T> {
    private HttpStatus status;
    private String mensagem;
    private T dados;
    private String transactionId;

    public ApiResponseDTO(HttpStatus status, String mensagem, T dados, String transactionId) {
        this.status = status;
        this.mensagem = mensagem;
        this.dados = dados;
        this.transactionId = transactionId;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public T getDados() {
        return dados;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
