package br.com.fiap.oficina.api.modules.catalogo.servico.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoSugeridoRequest(
                @NotNull(message = "O ID do produto (insumo) é obrigatório") @JsonProperty("produto_id") @Schema(description = "ID do Produto no Catálogo", defaultValue = "e0281660-5826-4c1f-8cab-238cdb1ac328") UUID produtoId,

                @NotNull(message = "A quantidade é obrigatória") @DecimalMin(value = "0.01", message = "A quantidade deve ser maior que zero") @Schema(description = "Quantidade necessária do produto para este serviço", defaultValue = "4.50") BigDecimal quantidade) {
}






