package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoItemRequest(
        @NotNull(message = "O ID do produto é obrigatório") @JsonProperty("produto_id") UUID produtoId,
        @NotNull(message = "A quantidade é obrigatória") @Min(value = 0, message = "A quantidade mínima é 0") BigDecimal quantidade) {
}






