package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.ZoneId;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdemDeServicoServicos {
    private static final ZoneId FUSO = ZoneId.of("America/Sao_Paulo");

    private UUID id;
    private UUID ordemDeServicoId;
    private UUID servicoId;
    private String nome;
    private int quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal total;
    private OrdemDeServicoServicoStatus status;
    private TipoServico tipo;
    private LocalDateTime dataInicioExecucao;
    private LocalDateTime dataFimExecucao;
    private UUID usuarioExecutorId;
    private java.util.List<OrdemDeServicoProdutos> produtos = new java.util.ArrayList<>();

    public OrdemDeServicoServicos() {}

    public OrdemDeServicoServicos(UUID ordemDeServicoId, UUID servicoId, String nome, int quantidade, BigDecimal precoUnitario, BigDecimal total, OrdemDeServicoServicoStatus status, TipoServico tipo) {
        this.id = UUID.randomUUID();
        this.ordemDeServicoId = ordemDeServicoId;
        this.servicoId = servicoId;
        this.nome = nome;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.total = total;
        this.status = status;
        this.tipo = tipo;
    }

    public OrdemDeServicoServicos(UUID id, UUID ordemDeServicoId, UUID servicoId, String nome, int quantidade, BigDecimal precoUnitario, BigDecimal total, OrdemDeServicoServicoStatus status, TipoServico tipo, LocalDateTime dataInicioExecucao, LocalDateTime dataFimExecucao, UUID usuarioExecutorId) {
        this.id = id;
        this.ordemDeServicoId = ordemDeServicoId;
        this.servicoId = servicoId;
        this.nome = nome;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.total = total;
        this.status = status;
        this.tipo = tipo;
        this.dataInicioExecucao = dataInicioExecucao;
        this.dataFimExecucao = dataFimExecucao;
        this.usuarioExecutorId = usuarioExecutorId;
    }

    public void calcularTotal() {
        BigDecimal totalServico = BigDecimal.ZERO;
        if (this.precoUnitario != null && this.quantidade != 0) {
            totalServico = this.precoUnitario.multiply(BigDecimal.valueOf(this.quantidade));
        }
        
        BigDecimal totalProdutos = produtos.stream()
                .map(OrdemDeServicoProdutos::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.total = totalServico.add(totalProdutos);
    }
    
    public BigDecimal getTotal() {
        if (this.total == null) calcularTotal();
        return this.total;
    }

    @JsonProperty("duracao")
    public String getDuracao() {
        if (dataInicioExecucao == null || dataFimExecucao == null) {
            return null;
        }
        // Com fuso, para a duração não sair errada se o intervalo cruzar uma
        // mudança de horário.
        Duration duration = Duration.between(dataInicioExecucao.atZone(FUSO), dataFimExecucao.atZone(FUSO));
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return String.format("%02dh %02dm", hours, minutes);
    }
}






