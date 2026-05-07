package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.infrastructure.persistence;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicos;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ORDEM_DE_SERVICO_SERVICOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrdemDeServicoServicosJpaEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "ordem_de_servico_id")
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

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "data_inicio_execucao")
    private java.time.LocalDateTime dataInicioExecucao;

    @Column(name = "data_fim_execucao")
    private java.time.LocalDateTime dataFimExecucao;

    @Column(name = "usuario_executor_id")
    private UUID usuarioExecutorId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "os_servico_id")
    private List<OrdemDeServicoProdutosJpaEntity> produtos = new ArrayList<>();

    public static OrdemDeServicoServicosJpaEntity fromDomain(OrdemDeServicoServicos servico) {
        OrdemDeServicoServicosJpaEntity entity = new OrdemDeServicoServicosJpaEntity();
        entity.setId(servico.getId());
        entity.setOrdemDeServicoId(servico.getOrdemDeServicoId());
        entity.setServicoId(servico.getServicoId());
        entity.setNome(servico.getNome());
        entity.setValorUnitario(servico.getPrecoUnitario());
        entity.setQuantidade(servico.getQuantidade());
        entity.setValorTotal(servico.getTotal());
        entity.setStatus(servico.getStatus().name());
        entity.setTipo(servico.getTipo().name());
        entity.setDataInicioExecucao(servico.getDataInicioExecucao());
        entity.setDataFimExecucao(servico.getDataFimExecucao());
        entity.setUsuarioExecutorId(servico.getUsuarioExecutorId());
        if (servico.getProdutos() != null) {
            entity.setProdutos(new ArrayList<>(servico.getProdutos().stream()
                .map(p -> OrdemDeServicoProdutosJpaEntity.fromDomain(p, servico.getId()))
                .toList()));
        }
        return entity;
    }

    public OrdemDeServicoServicos toDomain() {
        OrdemDeServicoServicos servico = new OrdemDeServicoServicos();
        servico.setId(this.id);
        servico.setOrdemDeServicoId(this.ordemDeServicoId);
        servico.setServicoId(this.servicoId);
        servico.setNome(this.nome);
        servico.setQuantidade(this.quantidade);
        servico.setPrecoUnitario(this.valorUnitario);
        servico.setTotal(this.valorTotal);
        servico.setStatus(OrdemDeServicoServicoStatus.fromString(this.status));
        servico.setTipo(TipoServico.valueOf(this.tipo));
        servico.setDataInicioExecucao(this.dataInicioExecucao);
        servico.setDataFimExecucao(this.dataFimExecucao);
        servico.setUsuarioExecutorId(this.usuarioExecutorId);
        if (this.produtos != null) {
            servico.setProdutos(new java.util.ArrayList<>(this.produtos.stream()
                .map(OrdemDeServicoProdutosJpaEntity::toDomain)
                .toList()));
        }
        return servico;
    }
}






