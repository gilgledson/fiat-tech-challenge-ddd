package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto;

import java.util.UUID;

public record VeiculoSnapshotDTO(
        UUID id,
        UUID clienteId,
        String placa,
        String modelo,
        String marca
) {}






