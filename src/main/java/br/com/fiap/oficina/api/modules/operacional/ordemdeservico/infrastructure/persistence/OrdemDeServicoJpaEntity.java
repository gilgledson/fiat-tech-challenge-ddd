package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.infrastructure.persistence;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;

@Entity
@Table(name = "ORDEM_DE_SERVICO")
@Getter
@Setter
public class OrdemDeServicoJpaEntity {
    @Id
    private UUID id;
    @Column(name = "cliente_id")
    private UUID clienteId;
    @Column(name = "veiculo_id")
    private UUID veiculoId;
    @Column(name = "descricao_problema")
    private String descricaoProblema;
    @Column(name = "status")
    private String status;
    @Column(name = "data_inicio_execucao")
    private LocalDateTime dataInicioExecucao;
    @Column(name = "data_fim_execucao")
    private LocalDateTime dataFimExecucao;
    @Column(name = "deletado_em")
    private LocalDateTime deletadoEm;
    @Column(name = "motivo_cancelamento")
    private String motivoCancelamento;

    @ElementCollection
    @CollectionTable(name = "ORDEM_DE_SERVICO_PRODUTOS", joinColumns = @JoinColumn(name = "ordem_de_servico_id"))
    private List<OrdemDeServicoProdutosEmbeddable> produtos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "ORDEM_DE_SERVICO_SERVICOS", joinColumns = @JoinColumn(name = "ordem_de_servico_id"))
    private List<OrdemDeServicoServicosEmbeddable> servicos = new ArrayList<>();

    public OrdemDeServico toDomain() {
        OrdemDeServico entity = new OrdemDeServico();
        entity.setId(id);
        entity.setClienteId(clienteId);
        entity.setVeiculoId(veiculoId);
        entity.setDescricaoProblema(descricaoProblema);
        entity.setStatus(OrdemDeServicoStatus.fromString(status));
        entity.setDataInicioExecucao(dataInicioExecucao);
        entity.setDataFimExecucao(dataFimExecucao);
        entity.setMotivoCancelamento(motivoCancelamento);
        entity.setDeletadoEm(deletadoEm);
        if (produtos != null) {
            entity.setProdutos(new ArrayList<>(produtos.stream()
                    .map(OrdemDeServicoProdutosEmbeddable::toDomain).toList()));
        }
        if (servicos != null) {
            entity.setServicos(new ArrayList<>(servicos.stream().map(OrdemDeServicoServicosEmbeddable::toDomain).toList()));
        }
        return entity;
    }

    public static OrdemDeServicoJpaEntity fromDomain(OrdemDeServico domain) {
        OrdemDeServicoJpaEntity entity = new OrdemDeServicoJpaEntity();
        entity.setId(domain.getId());
        entity.setClienteId(domain.getClienteId());
        entity.setVeiculoId(domain.getVeiculoId());
        entity.setDescricaoProblema(domain.getDescricaoProblema());
        entity.setStatus(domain.getStatus().name());
        entity.setDataInicioExecucao(domain.getDataInicioExecucao());
        entity.setDataFimExecucao(domain.getDataFimExecucao());
        entity.setMotivoCancelamento(domain.getMotivoCancelamento());
        entity.setDeletadoEm(domain.getDeletadoEm());
        if (domain.getProdutos() != null) {
            entity.setProdutos(
                    domain.getProdutos().stream().map(OrdemDeServicoProdutosEmbeddable::fromDomain).toList());
        }
        if (domain.getServicos() != null) {
            entity.setServicos(
                    domain.getServicos().stream().map(OrdemDeServicoServicosEmbeddable::fromDomain).toList());
        }
        return entity;
    }
}
