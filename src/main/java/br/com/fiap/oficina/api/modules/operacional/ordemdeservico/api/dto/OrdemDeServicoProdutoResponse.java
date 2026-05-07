package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

public record OrdemDeServicoProdutoResponse(
                @JsonProperty("produto_id") UUID produtoId,
                @Schema(description = "Nome do produto utilizado") String nome,
                @Schema(description = "Quantidade utilizada") BigDecimal quantidade,
                @JsonProperty("valor_unitario") @Schema(description = "Valor unitário cobrado") BigDecimal valorUnitario,
                @JsonProperty("valor_total") @Schema(description = "Valor total do produto (qtd x unitário)") BigDecimal valorTotal) {
}






