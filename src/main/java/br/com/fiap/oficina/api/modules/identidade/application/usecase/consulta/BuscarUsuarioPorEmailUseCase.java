package br.com.fiap.oficina.api.modules.identidade.application.usecase.consulta;

import java.util.Optional;

import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;

public interface BuscarUsuarioPorEmailUseCase {
    public Optional<Usuario> executar(String email);
}






