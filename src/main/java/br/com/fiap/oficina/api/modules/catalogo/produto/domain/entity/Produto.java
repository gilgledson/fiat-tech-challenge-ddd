package br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Getter
public class Produto {
    private UUID id;
    private String nome;
    private String codigoBarras;
    private BigDecimal precoUnitario;
    private BigDecimal quantidadeEstoqueFisico;
    private BigDecimal quantidadeEstoqueReservado;
    private UnidadeMedida unidadeMedida;
    private Optional<LocalDateTime> deletadoEm;

    public Produto(String nome, String codigoBarras, BigDecimal precoUnitario, BigDecimal quantidadeEstoqueFisico,
            BigDecimal quantidadeEstoqueReservado, UnidadeMedida unidadeMedida) {
        if (precoUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }
        if (quantidadeEstoqueFisico.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O estoque inicial não pode ser negativo.");
        }
        if (unidadeMedida == null) {
            throw new IllegalArgumentException("A unidade de medida é obrigatória.");
        }

        this.id = UUID.randomUUID();
        this.nome = nome;
        this.codigoBarras = codigoBarras;
        this.precoUnitario = precoUnitario;
        this.quantidadeEstoqueFisico = quantidadeEstoqueFisico;
        this.quantidadeEstoqueReservado = quantidadeEstoqueReservado;
        this.unidadeMedida = unidadeMedida;
        this.deletadoEm = Optional.empty();
    }

    private Produto(UUID id, String nome, String codigoBarras, BigDecimal precoUnitario,
            BigDecimal quantidadeEstoqueFisico, BigDecimal quantidadeEstoqueReservado, UnidadeMedida unidadeMedida,
            Optional<LocalDateTime> deletadoEm) {
        this.id = id;
        this.nome = nome;
        this.codigoBarras = codigoBarras;
        this.precoUnitario = precoUnitario;
        this.quantidadeEstoqueFisico = quantidadeEstoqueFisico;
        this.quantidadeEstoqueReservado = quantidadeEstoqueReservado;
        this.unidadeMedida = unidadeMedida;
        this.deletadoEm = deletadoEm;
    }

    public static Produto reconstituir(UUID id, String nome, String codigoBarras, BigDecimal precoUnitario,
            BigDecimal quantidadeEstoqueFisico, BigDecimal quantidadeEstoqueReservado, UnidadeMedida unidadeMedida,
            Optional<LocalDateTime> deletadoEm) {
        return new Produto(id, nome, codigoBarras, precoUnitario, quantidadeEstoqueFisico, quantidadeEstoqueReservado,
                unidadeMedida, deletadoEm);
    }

    public void inativar() {
        if (this.deletadoEm.isPresent()) {
            throw new IllegalArgumentException("Este produto já está inativo.");
        }
        this.deletadoEm = Optional.of(LocalDateTime.now());
    }

    public void ativar() {
        if (this.deletadoEm.isEmpty()) {
            throw new IllegalArgumentException("Este produto já está ativo.");
        }
        this.deletadoEm = Optional.empty();
    }

    public void atualizarDados(String nome, String codigoBarras, BigDecimal precoUnitario,
            BigDecimal quantidadeEstoqueFisico, BigDecimal quantidadeEstoqueReservado, UnidadeMedida unidadeMedida) {
        validarDados(precoUnitario, quantidadeEstoqueFisico, unidadeMedida);

        this.nome = nome;
        this.codigoBarras = codigoBarras;
        this.precoUnitario = precoUnitario;
        this.quantidadeEstoqueFisico = quantidadeEstoqueFisico;
        this.quantidadeEstoqueReservado = quantidadeEstoqueReservado;
        this.unidadeMedida = unidadeMedida;
    }

    private void validarDados(BigDecimal precoUnitario, BigDecimal quantidadeEstoqueFisico,
            UnidadeMedida unidadeMedida) {
        if (precoUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }
        if (quantidadeEstoqueFisico.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O estoque não pode ficar negativo.");
        }
        if (unidadeMedida == null) {
            throw new IllegalArgumentException("A unidade de medida é obrigatória.");
        }
    }

    public void deduzirEstoqueFisico(BigDecimal quantidade) {
        if (quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A quantidade a deduzir deve ser maior que zero.");
        }
        if (this.quantidadeEstoqueFisico.compareTo(quantidade) < 0) {
            throw new IllegalArgumentException("Estoque físico insuficiente para o produto: " + this.nome);
        }
        this.quantidadeEstoqueFisico = this.quantidadeEstoqueFisico.subtract(quantidade);
    }

    public void adicionarEstoqueFisico(BigDecimal quantidade) {
        if (quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A quantidade a adicionar deve ser maior que zero.");
        }
        this.quantidadeEstoqueFisico = this.quantidadeEstoqueFisico.add(quantidade);
    }

    public void reservarEstoque(BigDecimal quantidade) {
        if (quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A quantidade a reservar deve ser maior que zero.");
        }
        if (calcularDisponivel().compareTo(quantidade) < 0) {
            throw new IllegalArgumentException("Estoque disponível insuficiente para reservar: " + this.nome);
        }
        this.quantidadeEstoqueReservado = this.quantidadeEstoqueReservado.add(quantidade);
    }

    public void liberarEstoqueReservado(BigDecimal quantidade) {
        if (quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A quantidade a liberar deve ser maior que zero.");
        }
        if (this.quantidadeEstoqueReservado.compareTo(quantidade) < 0) {
            throw new IllegalArgumentException("Não há estoque reservado suficiente para liberar: " + this.nome);
        }
        this.quantidadeEstoqueReservado = this.quantidadeEstoqueReservado.subtract(quantidade);
    }

    public void confirmarVendaDeReserva(BigDecimal quantidade) {
        if (quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A quantidade a confirmar deve ser maior que zero.");
        }
        if (this.quantidadeEstoqueReservado.compareTo(quantidade) < 0) {
            throw new IllegalArgumentException(
                    "Não há estoque reservado suficiente para confirmar venda: " + this.nome);
        }
        // Sai do reservado e SAI do físico (foi embora da oficina)
        this.quantidadeEstoqueReservado = this.quantidadeEstoqueReservado.subtract(quantidade);
        this.quantidadeEstoqueFisico = this.quantidadeEstoqueFisico.subtract(quantidade);
    }

    public BigDecimal calcularDisponivel() {
        return this.quantidadeEstoqueFisico.subtract(this.quantidadeEstoqueReservado);
    }
}
