package br.com.fiap.oficina.api.modules.identidade.application.repository;

import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository {

    void salvar(Usuario usuario);

    Optional<Usuario> buscarPorEmail(String email);

    Optional<Usuario> buscarPorId(UUID id);

}






