package br.com.fiap.oficina.api.modules.atendimento.veiculo.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record VeiculoOutput(
        UUID id,
        UUID clienteId,
        String placa,
        String marca,
        String modelo,
        int ano,
        LocalDateTime deletadoEm
) {
}






