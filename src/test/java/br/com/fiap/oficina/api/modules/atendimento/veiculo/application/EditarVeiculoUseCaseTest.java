package br.com.fiap.oficina.api.modules.atendimento.veiculo.application;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.dto.VeiculoOutput;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.usecase.EditarVeiculoUseCaseImpl;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.domain.entity.Veiculo;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Editar Veículo - Testes Unitários")
class EditarVeiculoUseCaseTest {

    private VeiculoRepository repository;
    private EditarVeiculoUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(VeiculoRepository.class);
        useCase = new EditarVeiculoUseCaseImpl(repository);
    }

    @Test
    @DisplayName("Deve editar um veículo com sucesso")
    void deveEditarVeiculo() {
        UUID id = UUID.randomUUID();
        UUID clienteId = UUID.randomUUID();
        Veiculo veiculo = new Veiculo(clienteId, "ABC-1234", "Fiat", "Palio", 2010);
        
        when(repository.buscarPorId(id)).thenReturn(Optional.of(veiculo));

        VeiculoOutput output = useCase.executar(id, clienteId, "XYZ-9999", "VW", "Gol", 2020);

        assertEquals("XYZ-9999", output.placa());
        assertEquals("VW", output.marca());
        verify(repository).atualizar(any(Veiculo.class));
    }

    @Test
    @DisplayName("Deve lançar erro quando veículo não existe")
    void deveLancarErroVeiculoNaoExiste() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> 
            useCase.executar(id, UUID.randomUUID(), "PLACA", "MARCA", "MODELO", 2000)
        );
    }
}









