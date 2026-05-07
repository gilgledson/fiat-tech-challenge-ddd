package br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoSugeridoOutput(
        UUID produtoId,
        String nomeProduto,
        BigDecimal quantidade
) {
}






