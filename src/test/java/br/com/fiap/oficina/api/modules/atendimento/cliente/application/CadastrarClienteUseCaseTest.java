package br.com.fiap.oficina.api.modules.atendimento.cliente.application;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.dto.ClienteOutput;
import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.atendimento.cliente.application.usecase.CadastrarClienteUseCaseImpl;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.entity.Cliente;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.valueobject.Endereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CadastrarClienteUseCase - Testes Unitários")
class CadastrarClienteUseCaseTest {

    @Mock
    private ClienteRepository repository;

    private CadastrarClienteUseCaseImpl useCase;

    private Endereco enderecoValido() {
        return new Endereco("01001-000", "Praça da Sé", "s/n", null, "Sé", "São Paulo", "SP");
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CadastrarClienteUseCaseImpl(repository);
    }

    @Test
    @DisplayName("Deve cadastrar cliente com dados únicos e retornar ClienteOutput")
    void deveCadastrarComSucesso() {
        when(repository.buscarPorCpfCnpj("12345678901")).thenReturn(Optional.empty());
        UUID usuarioId = UUID.randomUUID();
        when(repository.buscarPorUsuarioId(usuarioId)).thenReturn(Optional.empty());

        ClienteOutput output = useCase.executar(usuarioId, "João Silva", "joao@email.com",
                "12345678901", "11999999999", enderecoValido());

        assertNotNull(output);
        assertEquals("João Silva", output.nome());
        verify(repository, times(1)).salvar(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF/CNPJ já cadastrado")
    void deveLancarExcecaoParaCpfCnpjDuplicado() {
        Cliente existente = new Cliente(null, "Outro", "outro@email.com", "12345678901", "11888888888", enderecoValido());
        when(repository.buscarPorCpfCnpj("12345678901")).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class, () ->
            useCase.executar(null, "João", "joao@email.com", "12345678901", "11999999999", enderecoValido())
        );
        verify(repository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuarioId já vinculado")
    void deveLancarExcecaoParaUsuarioIdDuplicado() {
        UUID usuarioId = UUID.randomUUID();
        Cliente existente = new Cliente(usuarioId, "Outro", "outro@email.com", "99999999999", "11888888888", enderecoValido());
        when(repository.buscarPorCpfCnpj("12345678901")).thenReturn(Optional.empty());
        when(repository.buscarPorUsuarioId(usuarioId)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class, () ->
            useCase.executar(usuarioId, "João", "joao@email.com", "12345678901", "11999999999", enderecoValido())
        );
        verify(repository, never()).salvar(any());
    }
}









