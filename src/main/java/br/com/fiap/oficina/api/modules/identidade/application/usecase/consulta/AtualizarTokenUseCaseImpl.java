package br.com.fiap.oficina.api.modules.identidade.application.usecase.consulta;

import java.time.Duration;
import java.util.UUID;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.com.fiap.oficina.api.modules.identidade.api.dto.TokenResponse;
import br.com.fiap.oficina.api.modules.identidade.application.repository.UsuarioRepository;
import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AtualizarTokenUseCaseImpl implements AtualizarTokenUseCase {
    private final UsuarioRepository repository;
    private final JWTParser jwtParser;

    @Override
    public TokenResponse executar(String refreshToken) {
        try {
            JsonWebToken jwt = jwtParser.parse(refreshToken);

            if (!"Refresh".equals(jwt.getClaim("typ"))) {
                throw new NotAuthorizedException("Token fornecido não é um Refresh Token válido.");
            }

            UUID idUsuario = UUID.fromString(jwt.getClaim("id"));

            Usuario usuario = repository.buscarPorId(idUsuario)
                    .filter(Usuario::isAtivo)
                    .orElseThrow(() -> new NotAuthorizedException("Usuário inativo ou não encontrado"));

            String novoAccessToken = Jwt.issuer("oficina-api-interna")
                    .upn(usuario.getEmail())
                    .groups(usuario.getPerfil().name())
                    .claim("usuario_id", usuario.getId().toString())
                    .claim("typ", "Bearer")
                    .expiresIn(Duration.ofMinutes(15))
                    .sign();

            String novoRefreshToken = Jwt.issuer("oficina-api-interna")
                    .upn(usuario.getEmail())
                    .claim("usuario_id", usuario.getId().toString())
                    .claim("typ", "Refresh")
                    .expiresIn(Duration.ofDays(7))
                    .sign();

            return new TokenResponse(novoAccessToken, Duration.ofMinutes(15).toMillis() / 1000, novoRefreshToken,
                    "Bearer");

        } catch (Exception e) {
            throw new NotAuthorizedException("Refresh token inválido");
        }
    }
}






