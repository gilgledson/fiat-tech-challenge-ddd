package br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Produto {
    private UUID id;
    private String nome;
    private String codigoBarras;
    private BigDecimal precoUnitario;
    private BigDecimal quantidadeEstoque;
    private UnidadeMedida unidadeMedida;
    private Optional<LocalDateTime> deletadoEm;

    public Produto(String nome, String codigoBarras, BigDecimal precoUnitario, BigDecimal quantidadeEstoque, UnidadeMedida unidadeMedida) {
        if (precoUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }
        if (quantidadeEstoque.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O estoque inicial não pode ser negativo.");
        }
        if (unidadeMedida == null) {
            throw new IllegalArgumentException("A unidade de medida é obrigatória.");
        }

        this.id = UUID.randomUUID();
        this.nome = nome;
        this.codigoBarras = codigoBarras;
        this.precoUnitario = precoUnitario;
        this.quantidadeEstoque = quantidadeEstoque;
        this.unidadeMedida = unidadeMedida;
        this.deletadoEm = Optional.empty();
    }

    public static Produto reconstituir(UUID id, String nome, String codigoBarras, BigDecimal precoUnitario, BigDecimal quantidadeEstoque, UnidadeMedida unidadeMedida, Optional<LocalDateTime> deletadoEm) {
        return new Produto(id, nome, codigoBarras, precoUnitario, quantidadeEstoque, unidadeMedida, deletadoEm);
    }

    public void inativar(){
        if (this.deletadoEm.isPresent()) {
            throw new IllegalArgumentException("Este produto já está inativo.");
        }
        this.deletadoEm = Optional.of(LocalDateTime.now());
    }

    public void ativar(){
        if (this.deletadoEm.isEmpty()) {
            throw new IllegalArgumentException("Este produto já está ativo.");
        }
        this.deletadoEm = Optional.empty();
    }

    public void atualizarDados(String nome, String codigoBarras, BigDecimal precoUnitario, BigDecimal quantidadeEstoque, UnidadeMedida unidadeMedida) {
        if (precoUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }
        if (quantidadeEstoque.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O estoque não pode ficar negativo.");
        }
        if (unidadeMedida == null) {
            throw new IllegalArgumentException("A unidade de medida é obrigatória.");
        }

        this.nome = nome;
        this.codigoBarras = codigoBarras;
        this.precoUnitario = precoUnitario;
        this.quantidadeEstoque = quantidadeEstoque;
        this.unidadeMedida = unidadeMedida;
    }
}
