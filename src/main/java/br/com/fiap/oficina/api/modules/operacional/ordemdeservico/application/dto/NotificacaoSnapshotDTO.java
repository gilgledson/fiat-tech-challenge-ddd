package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto;

import java.util.UUID;

public record NotificacaoSnapshotDTO(
    UUID clienteId,
    String titulo,
    String mensagem
) {}
