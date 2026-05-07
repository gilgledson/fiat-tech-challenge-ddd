package br.com.fiap.oficina.api.modules.identidade.application.usecase.consulta;

import br.com.fiap.oficina.api.modules.identidade.application.repository.UsuarioRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarUsuarioPorEmailUseCaseTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private BuscarUsuarioPorEmailUseCaseImpl useCase;

    @Test
    @DisplayName("Deve retornar usuário quando encontrado por email")
    void deveRetornarUsuarioQuandoEncontrado() {
        Usuario usuario = new Usuario("user@test.com", "hash", PerfilUsuario.CLIENTE);
        when(repository.buscarPorEmail("user@test.com")).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = useCase.executar("user@test.com");

        assertTrue(resultado.isPresent());
        assertEquals(usuario, resultado.get());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando email não encontrado")
    void deveRetornarVazioQuandoEmailNaoEncontrado() {
        when(repository.buscarPorEmail("nao@existe.com")).thenReturn(Optional.empty());

        Optional<Usuario> resultado = useCase.executar("nao@existe.com");

        assertTrue(resultado.isEmpty());
    }
}









