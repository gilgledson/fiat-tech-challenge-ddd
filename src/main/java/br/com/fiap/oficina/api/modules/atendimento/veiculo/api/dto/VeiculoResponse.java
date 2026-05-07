package br.com.fiap.oficina.api.modules.atendimento.veiculo.api.dto;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.dto.VeiculoOutput;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record VeiculoResponse(
        UUID id,
        @JsonProperty("cliente_id")
        UUID clienteId,
        String placa,
        String marca,
        String modelo,
        int ano,
        @JsonProperty("deletado_em")
        LocalDateTime deletadoEm
) {
    public static VeiculoResponse fromOutput(VeiculoOutput output) {
        if (output == null) return null;
        return new VeiculoResponse(
                output.id(),
                output.clienteId(),
                output.placa(),
                output.marca(),
                output.modelo(),
                output.ano(),
                output.deletadoEm()
        );
    }
}






