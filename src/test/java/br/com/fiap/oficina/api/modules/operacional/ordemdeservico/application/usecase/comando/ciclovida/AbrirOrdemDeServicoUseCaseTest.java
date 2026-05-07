package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.ciclovida;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.ClienteSnapshotDTO;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.AtendimentoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.VeiculoSnapshotDTO;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import io.vertx.core.eventbus.EventBus;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbrirOrdemDeServicoUseCaseTest {

    @Mock
    private OrdemDeServicoRepository repository;

    @Mock
    private AtendimentoGateway atendimentoGateway;

    @Mock
    private EventBus eventBus;

    @InjectMocks
    private AbrirOrdemDeServicoUseCaseImpl useCase;

    @Test
    @DisplayName("Deve abrir uma ordem de serviço com sucesso")
    void deveAbrirOrdemDeServicoComSucesso() {
        // Arrange
        UUID clienteId = UUID.randomUUID();
        UUID veiculoId = UUID.randomUUID();
        String descricao = "Motor falhando";

        when(atendimentoGateway.buscarClientePorId(clienteId)).thenReturn(Optional.of(new ClienteSnapshotDTO(clienteId, "Joao", "123")));
        when(atendimentoGateway.buscarVeiculoPorId(veiculoId)).thenReturn(Optional.of(new VeiculoSnapshotDTO(veiculoId, clienteId, "ABC-1234", "Uno", "Fiat")));

        // Act
        var output = useCase.executar(clienteId, veiculoId, descricao);

        // Assert
        assertNotNull(output.id());
        assertEquals(clienteId, output.clienteId());
        assertEquals(veiculoId, output.veiculoId());
        assertEquals(descricao, output.descricaoProblema());
        assertEquals(OrdemDeServicoStatus.ABERTA, output.status());
        
        verify(repository).salvar(any(OrdemDeServico.class));
        verify(eventBus).publish(anyString(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o veículo não pertence ao cliente")
    void deveLancarExcecaoVeiculoNaoPertenceAoCliente() {
        // Arrange
        UUID clienteId = UUID.randomUUID();
        UUID outroClienteId = UUID.randomUUID();
        UUID veiculoId = UUID.randomUUID();

        when(atendimentoGateway.buscarClientePorId(clienteId)).thenReturn(Optional.of(new ClienteSnapshotDTO(clienteId, "Joao", "123")));
        when(atendimentoGateway.buscarVeiculoPorId(veiculoId)).thenReturn(Optional.of(new VeiculoSnapshotDTO(veiculoId, outroClienteId, "ABC-1234", "Uno", "Fiat")));

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> 
            useCase.executar(clienteId, veiculoId, "Problema")
        );
        assertEquals("O veículo informado não pertence ao cliente informado.", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o cliente não é encontrado")
    void deveLancarExcecaoClienteNaoEncontrado() {
        UUID clienteId = UUID.randomUUID();
        when(atendimentoGateway.buscarClientePorId(clienteId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> 
            useCase.executar(clienteId, UUID.randomUUID(), "Problema")
        );
    }
}









