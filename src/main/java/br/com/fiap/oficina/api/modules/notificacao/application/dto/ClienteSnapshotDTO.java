package br.com.fiap.oficina.api.modules.notificacao.application.dto;

import java.util.Optional;
import java.util.UUID;

public record ClienteSnapshotDTO(
    UUID id,
    Optional<UUID> usuarioId,
    String email
) {}
