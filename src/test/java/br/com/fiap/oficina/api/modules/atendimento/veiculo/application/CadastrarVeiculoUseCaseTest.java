package br.com.fiap.oficina.api.modules.atendimento.veiculo.application;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.dto.VeiculoOutput;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.usecase.CadastrarVeiculoUseCaseImpl;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.domain.entity.Veiculo;
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

@DisplayName("CadastrarVeiculoUseCase - Testes Unitários")
class CadastrarVeiculoUseCaseTest {

    @Mock
    private VeiculoRepository repository;

    private CadastrarVeiculoUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CadastrarVeiculoUseCaseImpl(repository);
    }

    @Test
    @DisplayName("Deve cadastrar veículo com placa única e retornar VeiculoOutput")
    void deveCadastrarComSucesso() {
        UUID clienteId = UUID.randomUUID();
        when(repository.buscarPorPlaca("ABC-1234")).thenReturn(Optional.empty());

        VeiculoOutput output = useCase.executar(clienteId, "ABC-1234", "Toyota", "Corolla", 2022);

        assertNotNull(output);
        assertEquals("ABC-1234", output.placa());
        assertEquals(clienteId, output.clienteId());
        verify(repository, times(1)).salvar(any(Veiculo.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando placa já estiver cadastrada")
    void deveLancarExcecaoParaPlacaDuplicada() {
        UUID clienteId = UUID.randomUUID();
        Veiculo existente = new Veiculo(clienteId, "ABC-1234", "Honda", "Civic", 2021);
        when(repository.buscarPorPlaca("ABC-1234")).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class, () ->
            useCase.executar(clienteId, "ABC-1234", "Toyota", "Corolla", 2022)
        );
        verify(repository, never()).salvar(any());
    }
}









