package br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Servico {
    private UUID id;
    private String nome;
    private TipoServico tipo;
    private BigDecimal precoBase;
    private LocalDateTime deletadoEm;

    private List<ProdutoSugerido> produtosSugeridos;

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

    public List<ProdutoSugerido> getProdutosSugeridos() {
        return produtosSugeridos;
    }

    public void setProdutosSugeridos(List<ProdutoSugerido> produtosSugeridos) {
        this.produtosSugeridos = produtosSugeridos;
    }

    public Servico(UUID id, String nome, TipoServico tipo, BigDecimal precoBase, LocalDateTime deletadoEm,
            List<ProdutoSugerido> produtosSugeridos) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.precoBase = precoBase;
        this.deletadoEm = deletadoEm;
        this.produtosSugeridos = produtosSugeridos != null ? produtosSugeridos : new ArrayList<>();
    }

    public Servico(String nome, TipoServico tipo, BigDecimal precoBase, List<ProdutoSugerido> produtosSugeridos) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.tipo = tipo;
        this.precoBase = precoBase;
        this.deletadoEm = null;
        this.produtosSugeridos = produtosSugeridos != null ? produtosSugeridos : new ArrayList<>();

        validarEstado();
    }

    public void atualizar(String nome, TipoServico tipo, BigDecimal precoBase,
            List<ProdutoSugerido> produtosSugeridos) {
        this.nome = nome;
        this.tipo = tipo;
        this.precoBase = precoBase;
        this.deletadoEm = null;
        this.produtosSugeridos = produtosSugeridos != null ? produtosSugeridos : new ArrayList<>();

        validarEstado();
    }

    public void inativar() {
        if (this.deletadoEm != null) {
            throw new IllegalArgumentException("Este serviço já foi inativado anteriormente.");
        }
        this.deletadoEm = LocalDateTime.now();
    }

    public static Servico reconstituir(UUID id, String nome, TipoServico tipo, BigDecimal precoBase,
            LocalDateTime deletadoEm, List<ProdutoSugerido> produtosSugeridos) {
        return new Servico(id, nome, tipo, precoBase, deletadoEm, produtosSugeridos);
    }

    public void ativar() {
        if (this.deletadoEm == null) {
            throw new IllegalArgumentException("Este serviço já está ativo.");
        }
        this.deletadoEm = null;
    }

    private void validarEstado() {
        if (this.nome == null || this.nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do serviço não pode ser vazia.");
        }
        if (this.precoBase == null || this.precoBase.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço base deve ser maior que zero.");
        }
        if (this.tipo == null) {
            throw new IllegalArgumentException("O tipo de serviço é obrigatório.");
        }
    }
}






