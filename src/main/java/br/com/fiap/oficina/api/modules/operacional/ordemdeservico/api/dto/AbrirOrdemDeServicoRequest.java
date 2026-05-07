package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.UUID;

public record AbrirOrdemDeServicoRequest(
                @NotNull(message = "O ID do cliente é obrigatório") @Schema(description = "ID do cliente", defaultValue = "550e8400-e29b-41d4-a716-446655440000") @JsonProperty("cliente_id") UUID clienteId,

                @NotNull(message = "O ID do veículo é obrigatório") @Schema(description = "ID do veículo", defaultValue = "660e8400-e29b-41d4-a716-446655440000") @JsonProperty("veiculo_id") UUID veiculoId,

                @Schema(description = "Descrição do problema relatado pelo cliente", defaultValue = "Carro fazendo barulho ao frear") @JsonProperty("descricao_problema") String descricaoProblema) {
}






