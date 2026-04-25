package br.com.fiap.oficina.api.modules.identidade.application.usecase;

import br.com.fiap.oficina.api.modules.identidade.domain.entity.PerfilUsuario;
import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;

public interface CriarUsuarioUseCase {
    public Usuario executar(String email, String senhaPura, PerfilUsuario perfil);
}
