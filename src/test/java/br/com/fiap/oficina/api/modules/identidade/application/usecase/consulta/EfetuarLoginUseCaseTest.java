package br.com.fiap.oficina.api.modules.identidade.application.usecase.consulta;

import br.com.fiap.oficina.api.modules.identidade.application.repository.UsuarioRepository;
import br.com.fiap.oficina.api.modules.identidade.application.security.PasswordEncoder;
import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;
import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import jakarta.ws.rs.NotAuthorizedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EfetuarLoginUseCaseTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EfetuarLoginUseCaseImpl useCase;

    @Test
    @DisplayName("Deve lançar NotAuthorizedException quando usuário não for encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(repository.buscarPorEmail("inexistente@test.com")).thenReturn(Optional.empty());

        assertThrows(NotAuthorizedException.class,
                () -> useCase.executar("inexistente@test.com", "qualquer"));
    }

    @Test
    @DisplayName("Deve lançar NotAuthorizedException quando a senha estiver incorreta")
    void deveLancarExcecaoQuandoSenhaIncorreta() {
        Usuario usuario = new Usuario("user@test.com", "hash_correto", PerfilUsuario.ADMIN);

        when(repository.buscarPorEmail("user@test.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.verificar("senha_errada", "hash_correto")).thenReturn(false);

        assertThrows(NotAuthorizedException.class,
                () -> useCase.executar("user@test.com", "senha_errada"));
    }
}









