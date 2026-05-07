package br.com.fiap.oficina.api.modules.catalogo.produto.infrastructure.persistence;

import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.UnidadeMedida;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "PRODUTO")
public class ProdutoJpaEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "codigo_barras", unique = true)
    private String codigoBarras;

    @Version
    @Column(name = "versao")
    private Long versao;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(name = "quantidade_estoque_fisico", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeEstoqueFisico;

    @Column(name = "quantidade_estoque_reservado", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeEstoqueReservado;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidade_medida", nullable = false, length = 2)
    private UnidadeMedida unidadeMedida;

    @Column(name = "deletado_em")
    private LocalDateTime deletadoEm;

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

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public Long getVersao() {
        return versao;
    }

    public void setVersao(Long versao) {
        this.versao = versao;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public BigDecimal getQuantidadeEstoqueFisico() {
        return quantidadeEstoqueFisico;
    }

    public void setQuantidadeEstoqueFisico(BigDecimal quantidadeEstoqueFisico) {
        this.quantidadeEstoqueFisico = quantidadeEstoqueFisico;
    }

    public BigDecimal getQuantidadeEstoqueReservado() {
        return quantidadeEstoqueReservado;
    }

    public void setQuantidadeEstoqueReservado(BigDecimal quantidadeEstoqueReservado) {
        this.quantidadeEstoqueReservado = quantidadeEstoqueReservado;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public LocalDateTime getDeletadoEm() {
        return deletadoEm;
    }

    public void setDeletadoEm(LocalDateTime deletadoEm) {
        this.deletadoEm = deletadoEm;
    }

    public BigDecimal calcularEstoqueDisponivel() {
        return this.quantidadeEstoqueFisico.subtract(this.quantidadeEstoqueReservado);
    }

    public void reservar(BigDecimal quantidade) {
        if (quantidade.compareTo(calcularEstoqueDisponivel()) > 0) {
            throw new IllegalArgumentException("Estoque disponível insuficiente para reserva.");
        }
        this.quantidadeEstoqueReservado = this.quantidadeEstoqueReservado.add(quantidade);
    }

    public void liberarReserva(BigDecimal quantidade) {
        this.quantidadeEstoqueReservado = this.quantidadeEstoqueReservado.subtract(quantidade);
    }
}






