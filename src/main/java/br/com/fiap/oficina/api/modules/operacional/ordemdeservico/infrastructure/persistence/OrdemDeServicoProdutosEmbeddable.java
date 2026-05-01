package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.infrastructure.persistence;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoProdutos;
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
public class OrdemDeServicoProdutosEmbeddable {
    @Column(name = "ordem_de_servico_id", insertable = false, updatable = false)
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

    public static OrdemDeServicoProdutosEmbeddable fromDomain(OrdemDeServicoProdutos produto) {
        OrdemDeServicoProdutosEmbeddable embeddable = new OrdemDeServicoProdutosEmbeddable();
        embeddable.setOrdemDeServicoId(produto.getOrdemDeServicoId());
        embeddable.setProdutoId(produto.getProdutoId());
        embeddable.setNome(produto.getNomeDoProduto());
        embeddable.setQuantidade(produto.getQuantidade());
        embeddable.setPrecoUnitario(produto.getPrecoUnitario());
        embeddable.setValorTotal(produto.getTotal());
        return embeddable;
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
