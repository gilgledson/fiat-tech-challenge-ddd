package br.com.fiap.oficina.api.modules.notificacao.application.dto;

import java.util.UUID;

public record UsuarioSnapshotDTO(
    UUID id,
    String email,
    String perfil
) {}
