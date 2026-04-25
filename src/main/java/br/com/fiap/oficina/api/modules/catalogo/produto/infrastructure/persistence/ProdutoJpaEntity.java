package br.com.fiap.oficina.api.modules.catalogo.produto.infrastructure.persistence;

import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.UnidadeMedida;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "PRODUTO")
@Getter
@Setter
public class ProdutoJpaEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "codigo_barras", unique = true)
    private String codigoBarras;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(name = "quantidade_estoque", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeEstoque;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidade_medida", nullable = false, length = 2)
    private UnidadeMedida unidadeMedida;

    @Column(name = "deletado_em")
    private LocalDateTime deletadoEm;
}
