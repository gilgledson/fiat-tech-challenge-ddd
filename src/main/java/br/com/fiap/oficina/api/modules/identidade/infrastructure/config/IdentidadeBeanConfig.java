package br.com.fiap.oficina.api.modules.identidade.infrastructure.config;

import br.com.fiap.oficina.api.modules.identidade.application.repository.UsuarioRepository;
import br.com.fiap.oficina.api.modules.identidade.application.security.PasswordEncoder;
import br.com.fiap.oficina.api.modules.identidade.application.usecase.CriarUsuarioUseCase;
import br.com.fiap.oficina.api.modules.identidade.application.usecase.CriarUsuarioUseCaseImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;

@ApplicationScoped
public class IdentidadeBeanConfig {

    @Produces
    @ApplicationScoped
    public CriarUsuarioUseCase criarUsuarioUseCase(
            UsuarioRepository repository,
            PasswordEncoder encoder) {
        return new CriarUsuarioUseCaseImpl(repository, encoder);
    }
}
