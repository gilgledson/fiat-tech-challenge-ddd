package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdemDeServicoServicos {
    private UUID ordemDeServicoId;
    private UUID servicoId;
    private String nome;
    private int quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal total;
    private OrdemDeServicoServicoStatus status;
    private LocalDateTime dataInicioExecucao;
    private LocalDateTime dataFimExecucao;
    private UUID usuarioExecutorId;

    public OrdemDeServicoServicos() {}

    public OrdemDeServicoServicos(UUID ordemDeServicoId, UUID servicoId, String nome, int quantidade, BigDecimal precoUnitario, BigDecimal total, OrdemDeServicoServicoStatus status) {
        this.ordemDeServicoId = ordemDeServicoId;
        this.servicoId = servicoId;
        this.nome = nome;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.total = total;
        this.status = status;
    }

    public OrdemDeServicoServicos(UUID ordemDeServicoId, UUID servicoId, String nome, int quantidade, BigDecimal precoUnitario, BigDecimal total, OrdemDeServicoServicoStatus status, LocalDateTime dataInicioExecucao, LocalDateTime dataFimExecucao, UUID usuarioExecutorId) {
        this.ordemDeServicoId = ordemDeServicoId;
        this.servicoId = servicoId;
        this.nome = nome;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.total = total;
        this.status = status;
        this.dataInicioExecucao = dataInicioExecucao;
        this.dataFimExecucao = dataFimExecucao;
        this.usuarioExecutorId = usuarioExecutorId;
    }

    public void calcularTotal() {
        if (this.precoUnitario != null && this.quantidade != 0) {
            this.total = this.precoUnitario.multiply(BigDecimal.valueOf(this.quantidade));
        } else {
            this.total = BigDecimal.ZERO;
        }
    }

    @JsonProperty("duracao")
    public String getDuracao() {
        if (dataInicioExecucao == null || dataFimExecucao == null) {
            return null;
        }
        Duration duration = Duration.between(dataInicioExecucao, dataFimExecucao);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return String.format("%02dh %02dm", hours, minutes);
    }
}
