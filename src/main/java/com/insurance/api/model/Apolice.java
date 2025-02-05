package com.insurance.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Apolice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private String cpf;
    private String situacao;
    private BigDecimal premioTotal;
    private LocalDate dataCriacao;
    private LocalDate dataAlteracao;
    private Integer usuarioCriacao;
    private Integer usuarioAlteracao;

    @OneToMany(mappedBy = "apolice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parcela> parcelas;

    public Apolice() {}

    public Apolice(String descricao, String cpf, String situacao, BigDecimal premioTotal) {
        this.descricao = descricao;
        this.cpf = cpf;
        this.situacao = situacao;
        this.premioTotal = premioTotal;
        this.dataCriacao = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public BigDecimal getPremioTotal() {
        return premioTotal;
    }

    public void setPremioTotal(BigDecimal premioTotal) {
        this.premioTotal = premioTotal;
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

    public List<Parcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<Parcela> parcelas) {
        this.parcelas = parcelas;
    }
}