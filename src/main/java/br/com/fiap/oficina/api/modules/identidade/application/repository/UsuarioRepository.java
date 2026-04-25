package br.com.fiap.oficina.api.modules.identidade.application.repository;

import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository {

    void salvar(Usuario usuario);

    Optional<Usuario> buscarPorEmail(String email);

}