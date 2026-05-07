package br.com.fiap.oficina.api.modules.atendimento.cliente.application;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.atendimento.cliente.application.usecase.AtivarClienteUseCaseImpl;
import br.com.fiap.oficina.api.modules.atendimento.cliente.application.usecase.InativarClienteUseCaseImpl;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.entity.Cliente;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.valueobject.Endereco;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Ativar/Inativar Cliente - Testes Unitários")
class AtivarInativarClienteUseCaseTest {

    @Mock
    private ClienteRepository repository;

    private AtivarClienteUseCaseImpl ativarUseCase;
    private InativarClienteUseCaseImpl inativarUseCase;

    private Endereco enderecoValido() {
        return new Endereco("01001-000", "Praça da Sé", "s/n", null, "Sé", "São Paulo", "SP");
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ativarUseCase = new AtivarClienteUseCaseImpl(repository);
        inativarUseCase = new InativarClienteUseCaseImpl(repository);
    }

    // ---------- INATIVAR ----------

    @Test
    @DisplayName("Deve inativar cliente ativo com sucesso")
    void deveInativarComSucesso() {
        UUID id = UUID.randomUUID();
        Cliente cliente = Cliente.reconstituir(id, null, "João", "joao@email.com",
                "12345678901", "11999999999", enderecoValido(), Optional.empty());
        when(repository.buscarPorId(id)).thenReturn(Optional.of(cliente));

        inativarUseCase.executar(id);

        assertTrue(cliente.getDeletadoEm().isPresent());
        verify(repository, times(1)).atualizar(any(Cliente.class));
    }

    @Test
    @DisplayName("Inativar deve lançar NotFoundException quando cliente não existir")
    void inativarDeveLancarNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> inativarUseCase.executar(id));
    }

    @Test
    @DisplayName("Inativar cliente já inativo deve lançar IllegalArgumentException")
    void inativarClienteJaInativoDeveLancarExcecao() {
        UUID id = UUID.randomUUID();
        Cliente cliente = Cliente.reconstituir(id, null, "João", "joao@email.com",
                "12345678901", "11999999999", enderecoValido(), Optional.of(LocalDateTime.now()));
        when(repository.buscarPorId(id)).thenReturn(Optional.of(cliente));

        assertThrows(IllegalArgumentException.class, () -> inativarUseCase.executar(id));
    }

    // ---------- ATIVAR ----------

    @Test
    @DisplayName("Deve ativar cliente inativo com sucesso")
    void deveAtivarComSucesso() {
        UUID id = UUID.randomUUID();
        Cliente cliente = Cliente.reconstituir(id, null, "João", "joao@email.com",
                "12345678901", "11999999999", enderecoValido(), Optional.of(LocalDateTime.now()));
        when(repository.buscarPorId(id)).thenReturn(Optional.of(cliente));

        ativarUseCase.executar(id);

        assertTrue(cliente.getDeletadoEm().isEmpty());
        verify(repository, times(1)).atualizar(any(Cliente.class));
    }

    @Test
    @DisplayName("Ativar cliente já ativo deve lançar IllegalArgumentException")
    void ativarClienteJaAtivoDeveLancarExcecao() {
        UUID id = UUID.randomUUID();
        Cliente cliente = Cliente.reconstituir(id, null, "João", "joao@email.com",
                "12345678901", "11999999999", enderecoValido(), Optional.empty());
        when(repository.buscarPorId(id)).thenReturn(Optional.of(cliente));

        assertThrows(IllegalArgumentException.class, () -> ativarUseCase.executar(id));
    }
}









