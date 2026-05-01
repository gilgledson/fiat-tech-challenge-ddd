package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.infrastructure.persistence;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicos;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrdemDeServicoServicosEmbeddable {
    @Column(name = "ordem_de_servico_id", insertable = false, updatable = false)
    private UUID ordemDeServicoId;

    @Column(name = "servico_id")
    private UUID servicoId;

    @Column(name = "nome")
    private String nome;

    @Column(name = "quantidade")
    private int quantidade;

    @Column(name = "valor_unitario")
    private BigDecimal valorUnitario;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @Column(name = "status")
    private String status;

    @Column(name = "data_inicio_execucao")
    private java.time.LocalDateTime dataInicioExecucao;

    @Column(name = "data_fim_execucao")
    private java.time.LocalDateTime dataFimExecucao;

    @Column(name = "usuario_executor_id")
    private UUID usuarioExecutorId;

    public static OrdemDeServicoServicosEmbeddable fromDomain(OrdemDeServicoServicos servico) {
        OrdemDeServicoServicosEmbeddable servicoJpa = new OrdemDeServicoServicosEmbeddable();
        servicoJpa.setOrdemDeServicoId(servico.getOrdemDeServicoId());
        servicoJpa.setServicoId(servico.getServicoId());
        servicoJpa.setNome(servico.getNome());
        servicoJpa.setValorUnitario(servico.getPrecoUnitario());
        servicoJpa.setQuantidade(servico.getQuantidade());
        servicoJpa.setValorTotal(servico.getTotal());
        servicoJpa.setStatus(servico.getStatus().name());
        servicoJpa.setDataInicioExecucao(servico.getDataInicioExecucao());
        servicoJpa.setDataFimExecucao(servico.getDataFimExecucao());
        servicoJpa.setUsuarioExecutorId(servico.getUsuarioExecutorId());
        return servicoJpa;
    }

    public OrdemDeServicoServicos toDomain() {
        OrdemDeServicoServicos servico = new OrdemDeServicoServicos();
        servico.setOrdemDeServicoId(this.ordemDeServicoId);
        servico.setServicoId(this.servicoId);
        servico.setNome(this.nome);
        servico.setQuantidade(this.quantidade);
        servico.setPrecoUnitario(this.valorUnitario);
        servico.setTotal(this.valorTotal);
        servico.setStatus(OrdemDeServicoServicoStatus.fromString(this.status));
        servico.setDataInicioExecucao(this.dataInicioExecucao);
        servico.setDataFimExecucao(this.dataFimExecucao);
        servico.setUsuarioExecutorId(this.usuarioExecutorId);
        return servico;
    }
}
