package br.com.fiap.oficina.api.modules.faturamento.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.faturamento.domain.entity.Fatura;
import br.com.fiap.oficina.api.modules.faturamento.domain.valueObject.MetodoPagamento;
import br.com.fiap.oficina.api.modules.faturamento.domain.valueObject.StatusFatura;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "FATURA")
@Getter
@Setter
public class FaturaJpaEntity {
    public FaturaJpaEntity() {
    }

    public FaturaJpaEntity(UUID id, UUID ordemServicoId, LocalDate dataEmissao, LocalDate dataVencimento,
            BigDecimal valorTotal, StatusFatura status, MetodoPagamento metodoPagamento) {
        this.id = id;
        this.ordemServicoId = ordemServicoId;
        this.dataEmissao = dataEmissao;
        this.dataVencimento = dataVencimento;
        this.valorTotal = valorTotal;
        this.status = status;
        this.metodoPagamento = metodoPagamento;
    }

    @Id
    private UUID id;

    @Column(name = "ordem_servico_id", nullable = false)
    private UUID ordemServicoId;

    @Column(name = "data_emissao", nullable = false)
    private LocalDate dataEmissao;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "metodo_pagamento", nullable = true)
    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusFatura status;

    public Fatura toEntity() {
        return Fatura.reconstruir(
                id,
                dataEmissao,
                ordemServicoId,
                dataVencimento,
                valorTotal,
                status,
                metodoPagamento);
    }

    public static FaturaJpaEntity fromEntity(Fatura fatura) {
        return new FaturaJpaEntity(
                fatura.getId(),
                fatura.getOrdemServicoId(),
                fatura.getDataEmissao(),
                fatura.getDataVencimento(),
                fatura.getValorTotal(),
                fatura.getStatus(),
                fatura.getMetodoPagamento());
    }

}
