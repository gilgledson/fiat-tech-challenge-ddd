package br.com.fiap.oficina.api.modules.catalogo.servico.api.dto;

import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.dto.ProdutoSugeridoOutput;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoSugeridoResponse(
        @JsonProperty("produto_id") UUID produtoId,
        @JsonProperty("nome_produto") String nomeProduto,
        BigDecimal quantidade) {
    public static ProdutoSugeridoResponse fromOutput(ProdutoSugeridoOutput dominio) {
        return new ProdutoSugeridoResponse(dominio.produtoId(), dominio.nomeProduto(), dominio.quantidade());
    }
}






