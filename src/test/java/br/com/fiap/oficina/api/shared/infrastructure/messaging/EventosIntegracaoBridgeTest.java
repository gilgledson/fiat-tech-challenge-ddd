package br.com.fiap.oficina.api.shared.infrastructure.messaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.oficina.api.shared.infrastructure.persistence.outbox.OutboxEventoJpaEntity;
import br.com.fiap.oficina.api.shared.infrastructure.persistence.outbox.OutboxEventoRepository;
import io.vertx.core.json.JsonObject;

@ExtendWith(MockitoExtension.class)
class EventosIntegracaoBridgeTest {

    @Mock
    private OutboxEventoRepository repository;

    @InjectMocks
    private EventosIntegracaoBridge bridge;

    private final JsonObject payload = new JsonObject().put("ordemServicoId", UUID.randomUUID().toString());

    @Test
    @DisplayName("Cada evento interno da OS vai para a outbox com a routing key do contrato da Saga")
    void deveGravarCadaEventoComSuaRoutingKey() {
        bridge.onAberta(payload);
        bridge.onDiagnosticoConcluido(payload);
        bridge.onAprovada(payload);
        bridge.onRejeitada(payload);
        bridge.onExecucaoConcluida(payload);
        bridge.onPaga(payload);
        bridge.onEntregue(payload);
        bridge.onCancelada(payload);

        ArgumentCaptor<OutboxEventoJpaEntity> captor = ArgumentCaptor.forClass(OutboxEventoJpaEntity.class);
        verify(repository, times(8)).persist(captor.capture());
        assertEquals(List.of("os.aberta", "os.diagnostico-concluido", "os.aprovada", "os.rejeitada",
                "os.execucao-concluida", "os.paga", "os.entregue", "os.cancelada"),
                captor.getAllValues().stream().map(OutboxEventoJpaEntity::getRoutingKey).toList());
    }

    @Test
    @DisplayName("O evento é gravado como pendente, com o payload original")
    void deveGravarEventoPendente() {
        bridge.onAberta(payload);

        ArgumentCaptor<OutboxEventoJpaEntity> captor = ArgumentCaptor.forClass(OutboxEventoJpaEntity.class);
        verify(repository).persist(captor.capture());
        OutboxEventoJpaEntity gravado = captor.getValue();
        assertNotNull(gravado.getId());
        assertNotNull(gravado.getCriadoEm());
        assertNull(gravado.getPublicadoEm());
        assertEquals(payload, new JsonObject(gravado.getPayload()));
    }
}
