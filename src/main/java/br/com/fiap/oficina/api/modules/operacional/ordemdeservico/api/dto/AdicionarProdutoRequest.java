package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

public record AdicionarProdutoRequest(
                @NotNull(message = "O ID do produto é obrigatório") @Schema(description = "ID do produto do catálogo", defaultValue = "a1b2c3d4-0000-0000-0000-000000000001") @JsonProperty("produto_id") UUID produtoId,

                @DecimalMin(value = "0.01", message = "A quantidade mínima é 0.01") @Schema(description = "Quantidade a ser adicionada", defaultValue = "0.50") BigDecimal quantidade) {
}






