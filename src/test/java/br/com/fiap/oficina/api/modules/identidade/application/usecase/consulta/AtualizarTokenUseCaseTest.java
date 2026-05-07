package br.com.fiap.oficina.api.modules.identidade.application.usecase.consulta;

import br.com.fiap.oficina.api.modules.identidade.application.repository.UsuarioRepository;
import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;
import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import io.smallrye.jwt.auth.principal.JWTParser;
import jakarta.ws.rs.NotAuthorizedException;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarTokenUseCaseTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private JWTParser jwtParser;

    @InjectMocks
    private AtualizarTokenUseCaseImpl useCase;

    @Test
    @DisplayName("Deve lançar NotAuthorizedException quando parse do token falhar")
    void deveLancarExcecaoQuandoTokenInvalido() throws Exception {
        when(jwtParser.parse(anyString())).thenThrow(new RuntimeException("parse error"));

        assertThrows(NotAuthorizedException.class,
                () -> useCase.executar("token_invalido"));
    }

    @Test
    @DisplayName("Deve lançar NotAuthorizedException quando token não for do tipo Refresh")
    void deveLancarExcecaoQuandoTipoNaoEhRefresh() throws Exception {
        JsonWebToken mockJwt = mock(JsonWebToken.class);
        when(jwtParser.parse(anyString())).thenReturn(mockJwt);
        doReturn("Bearer").when(mockJwt).getClaim("typ");

        assertThrows(NotAuthorizedException.class,
                () -> useCase.executar("token_access"));
    }

    @Test
    @DisplayName("Deve lançar NotAuthorizedException quando usuário não for encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() throws Exception {
        JsonWebToken mockJwt = mock(JsonWebToken.class);
        when(jwtParser.parse(anyString())).thenReturn(mockJwt);
        doReturn("Refresh").when(mockJwt).getClaim("typ");
        doReturn(UUID.randomUUID().toString()).when(mockJwt).getClaim("id");
        when(repository.buscarPorId(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NotAuthorizedException.class,
                () -> useCase.executar("refresh_token"));
    }

    @Test
    @DisplayName("Deve lançar NotAuthorizedException quando usuário estiver inativo")
    void deveLancarExcecaoQuandoUsuarioInativo() throws Exception {
        JsonWebToken mockJwt = mock(JsonWebToken.class);
        UUID userId = UUID.randomUUID();
        Usuario usuario = new Usuario(userId, "user@test.com", "hash", PerfilUsuario.CLIENTE, false);

        when(jwtParser.parse(anyString())).thenReturn(mockJwt);
        doReturn("Refresh").when(mockJwt).getClaim("typ");
        doReturn(userId.toString()).when(mockJwt).getClaim("id");
        when(repository.buscarPorId(userId)).thenReturn(Optional.of(usuario));

        assertThrows(NotAuthorizedException.class,
                () -> useCase.executar("refresh_token"));
    }
}









