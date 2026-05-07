package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado;

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
class IniciarExecucaoOsUseCaseTest {

    @Mock
    private OrdemDeServicoRepository repository;

    @Mock
    private EventBus eventBus;

    @InjectMocks
    private IniciarExecucaoOsUseCaseImpl useCase;

    @Test
    @DisplayName("Deve iniciar execução de OS APROVADA com sucesso")
    void deveIniciarExecucaoComSucesso() {
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.APROVADA);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        useCase.executar(osId);

        assertEquals(OrdemDeServicoStatus.EM_EXECUCAO, os.getStatus());
        assertNotNull(os.getDataInicioExecucao());
        verify(repository).atualizar(os);
        verify(eventBus).publish(anyString(), any());
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
    @DisplayName("Deve lançar exceção quando OS não estiver APROVADA")
    void deveLancarExcecaoQuandoOsNaoAprovada() {
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.ABERTA);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.executar(osId));
        assertTrue(ex.getMessage().contains("APROVADA"));
        verify(repository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando OS estiver em AGUARDANDO_APROVACAO")
    void deveLancarExcecaoQuandoOsAguardandoAprovacao() {
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.AGUARDANDO_APROVACAO);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        assertThrows(IllegalArgumentException.class, () -> useCase.executar(osId));
    }
}









