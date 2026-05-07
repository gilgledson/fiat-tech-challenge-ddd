package br.com.fiap.oficina.api.modules.identidade.api.dto;

import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;
import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import java.util.UUID;

public record UsuarioResponse(
        UUID id,
        String email,
        PerfilUsuario perfil,
        boolean ativo) {
    public static UsuarioResponse fromDomain(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.isAtivo());
    }
}






