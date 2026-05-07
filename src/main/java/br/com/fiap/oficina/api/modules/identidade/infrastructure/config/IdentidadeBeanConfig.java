package br.com.fiap.oficina.api.modules.identidade.infrastructure.config;

import br.com.fiap.oficina.api.modules.identidade.application.repository.UsuarioRepository;
import br.com.fiap.oficina.api.modules.identidade.application.security.PasswordEncoder;
import br.com.fiap.oficina.api.modules.identidade.application.usecase.comandos.CriarUsuarioUseCase;
import br.com.fiap.oficina.api.modules.identidade.application.usecase.comandos.CriarUsuarioUseCaseImpl;
import br.com.fiap.oficina.api.modules.identidade.application.usecase.consulta.AtualizarTokenUseCase;
import br.com.fiap.oficina.api.modules.identidade.application.usecase.consulta.AtualizarTokenUseCaseImpl;
import br.com.fiap.oficina.api.modules.identidade.application.usecase.consulta.EfetuarLoginUseCase;
import br.com.fiap.oficina.api.modules.identidade.application.usecase.consulta.EfetuarLoginUseCaseImpl;
import io.smallrye.jwt.auth.principal.JWTParser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class IdentidadeBeanConfig {

    @Produces
    @ApplicationScoped
    public CriarUsuarioUseCase criarUsuarioUseCase(
            UsuarioRepository repository,
            PasswordEncoder encoder) {
        return new CriarUsuarioUseCaseImpl(repository, encoder);
    }

    @Produces
    @ApplicationScoped
    public AtualizarTokenUseCase atualizarTokenUseCase(
            UsuarioRepository repository,
            JWTParser jwtParser) {
        return new AtualizarTokenUseCaseImpl(repository, jwtParser);
    }

    @Produces
    @ApplicationScoped
    public EfetuarLoginUseCase efetuarLoginUseCase(
            UsuarioRepository repository,
            PasswordEncoder encoder) {
        return new EfetuarLoginUseCaseImpl(repository, encoder);
    }

}






