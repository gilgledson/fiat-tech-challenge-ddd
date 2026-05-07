package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto;

import java.util.UUID;

public record ClienteSnapshotDTO(
        UUID id,
        String nome,
        String cpfCnpj
) {}






