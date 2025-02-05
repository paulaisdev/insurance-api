package com.insurance.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Parcela {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_apolice")
    private Apolice apolice;

    private BigDecimal premio;
    private String formaPagamento;
    private LocalDate dataPagamento;
    private LocalDate dataPago;
    private BigDecimal juros;

    @Column(nullable = false)
    private String situacao = "PENDENTE";

    private LocalDate dataCriacao;
    private LocalDate dataAlteracao;
    private Integer usuarioCriacao;
    private Integer usuarioAlteracao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Apolice getApolice() {
        return apolice;
    }

    public void setApolice(Apolice apolice) {
        this.apolice = apolice;
    }

    public BigDecimal getPremio() {
        return premio;
    }

    public void setPremio(BigDecimal premio) {
        this.premio = premio;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public LocalDate getDataPago() {
        return dataPago;
    }

    public void setDataPago(LocalDate dataPago) {
        this.dataPago = dataPago;
    }

    public BigDecimal getJuros() {
        return juros;
    }

    public void setJuros(BigDecimal juros) {
        this.juros = juros;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDate dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public Integer getUsuarioCriacao() {
        return usuarioCriacao;
    }

    public void setUsuarioCriacao(Integer usuarioCriacao) {
        this.usuarioCriacao = usuarioCriacao;
    }

    public Integer getUsuarioAlteracao() {
        return usuarioAlteracao;
    }

    public void setUsuarioAlteracao(Integer usuarioAlteracao) {
        this.usuarioAlteracao = usuarioAlteracao;
    }
}