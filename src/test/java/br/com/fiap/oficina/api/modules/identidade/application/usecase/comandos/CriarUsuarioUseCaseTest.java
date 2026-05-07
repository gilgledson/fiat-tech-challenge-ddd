package br.com.fiap.oficina.api.modules.identidade.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.identidade.application.repository.UsuarioRepository;
import br.com.fiap.oficina.api.modules.identidade.application.security.PasswordEncoder;
import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;
import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CriarUsuarioUseCaseImpl useCase;

    @Test
    @DisplayName("Deve criar usuário com senha criptografada e salvar no repositório")
    void deveCriarUsuarioComSucesso() {
        String email = "novo@test.com";
        String senhaPura = "senha123";
        String senhaHash = "hash_criptografado";

        when(repository.buscarPorEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.criptografar(senhaPura)).thenReturn(senhaHash);

        Usuario resultado = useCase.executar(email, senhaPura, PerfilUsuario.MECANICO);

        assertNotNull(resultado);
        assertEquals(email, resultado.getEmail());
        assertEquals(senhaHash, resultado.getSenhaHash());
        assertEquals(PerfilUsuario.MECANICO, resultado.getPerfil());
        assertTrue(resultado.isAtivo());
        verify(repository).salvar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já estiver cadastrado")
    void deveLancarExcecaoQuandoEmailJaExiste() {
        String email = "existente@test.com";
        Usuario existente = new Usuario(email, "hash", PerfilUsuario.CLIENTE);

        when(repository.buscarPorEmail(email)).thenReturn(Optional.of(existente));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.executar(email, "senha", PerfilUsuario.CLIENTE));
        assertTrue(ex.getMessage().contains("e-mail"));
        verify(repository, never()).salvar(any());
    }
}









