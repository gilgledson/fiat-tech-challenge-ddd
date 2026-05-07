package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.UUID;

public record AdicionarServicoRequest(
                @NotNull(message = "O ID do serviço é obrigatório") @Schema(description = "ID do serviço do catálogo", defaultValue = "00000000-0000-0000-0000-000000000000") @JsonProperty("servico_id") UUID servicoId,

                @Min(value = 1, message = "A quantidade mínima é 1") @Schema(description = "Quantidade a ser adicionada", defaultValue = "1") int quantidade,
                @Schema(description = "Lista de produtos associados ao serviço") java.util.List<ProdutoItemRequest> produtos) {
}






