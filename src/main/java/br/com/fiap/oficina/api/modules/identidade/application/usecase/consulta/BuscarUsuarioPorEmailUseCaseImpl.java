package br.com.fiap.oficina.api.modules.identidade.application.usecase.consulta;

import java.util.Optional;

import br.com.fiap.oficina.api.modules.identidade.application.repository.UsuarioRepository;
import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BuscarUsuarioPorEmailUseCaseImpl implements BuscarUsuarioPorEmailUseCase {

    private final UsuarioRepository repository;

    @Override
    public Optional<Usuario> executar(String email) {
        return repository.buscarPorEmail(email);
    }
}






