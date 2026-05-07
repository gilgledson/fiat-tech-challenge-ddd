package br.com.fiap.oficina.api.modules.atendimento.cliente.application;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.atendimento.cliente.application.usecase.EditarClienteUseCaseImpl;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.entity.Cliente;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.valueobject.Endereco;
import jakarta.ws.rs.NotFoundException;
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

@DisplayName("EditarClienteUseCase - Testes Unitários")
class EditarClienteUseCaseTest {

    @Mock
    private ClienteRepository repository;

    private EditarClienteUseCaseImpl useCase;

    private Endereco enderecoValido() {
        return new Endereco("01001-000", "Praça da Sé", "s/n", null, "Sé", "São Paulo", "SP");
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new EditarClienteUseCaseImpl(repository);
    }

    @Test
    @DisplayName("Deve editar cliente existente e chamar atualizar")
    void deveEditarComSucesso() {
        UUID id = UUID.randomUUID();
        Cliente cliente = Cliente.reconstituir(id, null, "João Antigo", "joao@email.com",
                "12345678901", "11999999999", enderecoValido(), Optional.empty());
        when(repository.buscarPorId(id)).thenReturn(Optional.of(cliente));

        var output = useCase.executar(id, "João Novo", "novo@email.com",
                "11988888888", enderecoValido());

        assertEquals("João Novo", output.nome());
        verify(repository, times(1)).atualizar(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando cliente não existir")
    void deveLancarNotFoundQuandoClienteNaoExistir() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
            useCase.executar(id, "Nome", "email@email.com", "11999999999", enderecoValido())
        );
    }
}









