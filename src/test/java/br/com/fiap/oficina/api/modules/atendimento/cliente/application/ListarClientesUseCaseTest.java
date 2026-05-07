package br.com.fiap.oficina.api.modules.atendimento.cliente.application;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.dto.ClienteOutput;
import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.atendimento.cliente.application.usecase.ListarClientesUseCaseImpl;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.entity.Cliente;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.valueobject.Endereco;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DisplayName("Listar Clientes - Testes Unitários")
class ListarClientesUseCaseTest {

    private ClienteRepository repository;
    private ListarClientesUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ClienteRepository.class);
        useCase = new ListarClientesUseCaseImpl(repository);
    }

    @Test
    @DisplayName("Deve listar clientes paginados")
    void deveListarClientes() {
        Cliente cliente = Cliente.reconstituir(
                UUID.randomUUID(), 
                UUID.randomUUID(), 
                "João", 
                "joao@email.com", 
                "12345678901", 
                "11999999999", 
                Mockito.mock(Endereco.class), 
                Optional.empty()
        );
        Pagina<Cliente> paginaMock = new Pagina<>(List.of(cliente), 0, 10, 1, 1);
        
        when(repository.listarTodos(anyInt(), anyInt(), anyBoolean())).thenReturn(paginaMock);

        Pagina<ClienteOutput> resultado = useCase.executar(0, 10, false);

        assertEquals(1, resultado.itens().size());
        assertEquals("João", resultado.itens().get(0).nome());
        assertEquals(0, resultado.paginaAtual());
        assertEquals(1, resultado.totalElementos());
    }
}









