package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto;

import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record CancelarOrdemRequest(
                @NotBlank(message = "O motivo do cancelamento é obrigatório") @Schema(description = "Motivo pelo qual a ordem está sendo cancelada", defaultValue = "Cliente desistiu do reparo") String motivo) {
}






