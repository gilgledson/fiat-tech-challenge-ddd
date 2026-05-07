package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.infrastructure.persistence;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoProdutos;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ORDEM_DE_SERVICO_PRODUTOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrdemDeServicoProdutosJpaEntity {
    @Id
    private UUID id;

    @Column(name = "os_servico_id")
    private UUID osServicoId;

    @Column(name = "ordem_de_servico_id")
    private UUID ordemDeServicoId;

    @Column(name = "produto_id")
    private UUID produtoId;

    @Column(name = "nome")
    private String nome;

    @Column(name = "quantidade")
    private BigDecimal quantidade;

    @Column(name = "valor_unitario")
    private BigDecimal precoUnitario;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    public static OrdemDeServicoProdutosJpaEntity fromDomain(OrdemDeServicoProdutos produto, UUID osServicoId) {
        OrdemDeServicoProdutosJpaEntity entity = new OrdemDeServicoProdutosJpaEntity();
        entity.setId(UUID.randomUUID()); // We need a primary key for the entity
        entity.setOsServicoId(osServicoId);
        entity.setOrdemDeServicoId(produto.getOrdemDeServicoId());
        entity.setProdutoId(produto.getProdutoId());
        entity.setNome(produto.getNomeDoProduto());
        entity.setQuantidade(produto.getQuantidade());
        entity.setPrecoUnitario(produto.getPrecoUnitario());
        entity.setValorTotal(produto.getTotal());
        return entity;
    }

    public OrdemDeServicoProdutos toDomain() {
        OrdemDeServicoProdutos domain = new OrdemDeServicoProdutos();
        domain.setOrdemDeServicoId(this.ordemDeServicoId);
        domain.setProdutoId(this.produtoId);
        domain.setNomeDoProduto(this.nome);
        domain.setQuantidade(this.quantidade);
        domain.setPrecoUnitario(this.precoUnitario);
        domain.setTotal(this.valorTotal);
        return domain;
    }
}






