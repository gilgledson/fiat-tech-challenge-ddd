package br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoSugerido(
        UUID produtoId,
        BigDecimal quantidade
) {
    // Construtor compacto do Record para validação de regras de negócio
    public ProdutoSugerido {
        if (produtoId == null) {
            throw new IllegalArgumentException("O ID do produto sugerido é obrigatório.");
        }
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A quantidade sugerida deve ser maior que zero.");
        }
    }
}






