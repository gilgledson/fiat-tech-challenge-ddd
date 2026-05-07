package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcluirDiagnosticoUseCaseTest {

    @Mock
    private OrdemDeServicoRepository repository;

    @Mock
    private EventBus eventBus;

    @InjectMocks
    private ConcluirDiagnosticoUseCaseImpl useCase;

    @Test
    @DisplayName("Deve concluir diagnóstico e mover OS para AGUARDANDO_APROVACAO")
    void deveConcluirDiagnosticoComSucesso() {
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.EM_DIAGNOSTICO);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        OrdemDeServicoOutput resultado = useCase.executar(osId);

        assertEquals(OrdemDeServicoStatus.AGUARDANDO_APROVACAO, os.getStatus());
        verify(repository).atualizar(os);
        verify(eventBus).publish(anyString(), any());
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("Deve lançar exceção quando OS não for encontrada")
    void deveLancarExcecaoQuandoOsNaoEncontrada() {
        UUID osId = UUID.randomUUID();
        when(repository.buscarPorId(osId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.executar(osId));
        verify(repository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando OS estiver CANCELADA")
    void deveLancarExcecaoQuandoOsCancelada() {
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.CANCELADA);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.executar(osId));
        assertTrue(ex.getMessage().contains("encerrada"));
        verify(repository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando OS estiver REJEITADA")
    void deveLancarExcecaoQuandoOsRejeitada() {
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.REJEITADA);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        assertThrows(IllegalArgumentException.class, () -> useCase.executar(osId));
    }
}









