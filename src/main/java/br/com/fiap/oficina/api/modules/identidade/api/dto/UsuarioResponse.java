package br.com.fiap.oficina.api.modules.identidade.api.dto;

import br.com.fiap.oficina.api.modules.identidade.domain.entity.PerfilUsuario;

import java.util.UUID;

public record UsuarioResponse(
        UUID id,
        String email,
        PerfilUsuario perfil,
        boolean ativo
) {}
