package br.com.fiap.oficina.api.modules.identidade.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;

public interface CriarUsuarioUseCase {
    public Usuario executar(String email, String senhaPura, PerfilUsuario perfil);
}






