package br.com.fiap.oficina.api.modules.atendimento.veiculo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.UUID;

public record VeiculoRequest(
                @NotNull(message = "O ID do cliente é obrigatório") @Schema(description = "ID do cliente dono do veículo", defaultValue = "123e4567-e89b-12d3-a456-426614174000") @JsonProperty("cliente_id") UUID clienteId,

                @NotBlank(message = "A placa é obrigatória") @Schema(description = "Placa do veículo", defaultValue = "ABC-1234") String placa,

                @NotBlank(message = "A marca é obrigatória") @Schema(description = "Marca do veículo", defaultValue = "Toyota") String marca,

                @NotBlank(message = "O modelo é obrigatório") @Schema(description = "Modelo do veículo", defaultValue = "Corolla") String modelo,

                @NotNull(message = "O ano é obrigatório") @Schema(description = "Ano do veículo", defaultValue = "2023") int ano) {
}






