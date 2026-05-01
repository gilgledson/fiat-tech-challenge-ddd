package br.com.fiap.oficina.api.modules.catalogo.servico.infrastructure.persistence;

import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "SERVICO")
@Getter
@Setter
public class ServicoJpaEntity {
    @Id
    private UUID id;

    @Column
    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoServico tipo;

    @Column(name = "preco_base")
    private BigDecimal precoBase;

    @Column(name = "deletado_em")
    private LocalDateTime deletadoEm;

    @ElementCollection
    @CollectionTable(name = "SERVICO_PRODUTO_SUGERIDO", joinColumns = @JoinColumn(name = "servico_id"))
    private List<ProdutoSugeridoEmbeddable> produtosSugeridos = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoServico getTipo() {
        return tipo;
    }

    public void setTipo(TipoServico tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(BigDecimal precoBase) {
        this.precoBase = precoBase;
    }

    public LocalDateTime getDeletadoEm() {
        return deletadoEm;
    }

    public void setDeletadoEm(LocalDateTime deletadoEm) {
        this.deletadoEm = deletadoEm;
    }

    public List<ProdutoSugeridoEmbeddable> getProdutosSugeridos() {
        return produtosSugeridos;
    }

    public void setProdutosSugeridos(List<ProdutoSugeridoEmbeddable> produtosSugeridos) {
        this.produtosSugeridos = produtosSugeridos;
    }
}
