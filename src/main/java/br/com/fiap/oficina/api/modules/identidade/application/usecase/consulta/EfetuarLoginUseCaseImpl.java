package br.com.fiap.oficina.api.modules.identidade.application.usecase.consulta;

import java.time.Duration;

import br.com.fiap.oficina.api.modules.identidade.api.dto.TokenResponse;
import br.com.fiap.oficina.api.modules.identidade.application.repository.UsuarioRepository;
import br.com.fiap.oficina.api.modules.identidade.application.security.PasswordEncoder;
import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;
import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EfetuarLoginUseCaseImpl implements EfetuarLoginUseCase {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponse executar(String email, String senha) {
        Usuario usuario = repository.buscarPorEmail(email)
                .orElseThrow(() -> new NotAuthorizedException("Usuario ou senha invalidos"));

        if (!passwordEncoder.verificar(senha, usuario.getSenhaHash())) {
            throw new NotAuthorizedException("Usuario ou senha invalidos");
        }

        String token = Jwt.issuer("oficina-api-interna")
                .upn(usuario.getEmail())
                .groups(usuario.getPerfil().name())
                .claim("usuario_id", usuario.getId().toString())
                .expiresIn(Duration.ofHours(8))
                .sign();

        String refreshToken = Jwt.issuer("oficina-api-interna")
                .upn(usuario.getEmail())
                .claim("usuario_id", usuario.getId().toString())
                .claim("typ", "Refresh")
                .expiresIn(Duration.ofDays(7))
                .sign();

        return new TokenResponse(token, Duration.ofHours(8).toMillis() / 1000, refreshToken, "Bearer");
    }
}






