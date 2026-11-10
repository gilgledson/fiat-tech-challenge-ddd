package br.com.fiap.oficina.api.shared.infrastructure.messaging;

import java.time.Instant;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoAberta;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoAprovada;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoCancelada;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoDiagnosticoConcluido;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoEntregue;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoExecucaoConcluidaEvent;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoPaga;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoRejeitada;
import br.com.fiap.oficina.api.shared.infrastructure.persistence.outbox.OutboxEventoJpaEntity;
import br.com.fiap.oficina.api.shared.infrastructure.persistence.outbox.OutboxEventoRepository;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Ponte entre o barramento interno (Vert.x EventBus) e o RabbitMQ: os eventos
 * da OS que interessam aos outros microsserviços são gravados na outbox com a
 * routing key do contrato da Saga, e o {@link OutboxRelay} os entrega ao broker.
 *
 * Os casos de uso continuam publicando só no barramento interno, como no
 * monólito. O custo disso é que a gravação na outbox acontece logo depois da
 * gravação da OS, não na mesma transação.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class EventosIntegracaoBridge {

    public static final String OS_ABERTA = "os.aberta";
    public static final String OS_DIAGNOSTICO_CONCLUIDO = "os.diagnostico-concluido";
    public static final String OS_APROVADA = "os.aprovada";
    public static final String OS_REJEITADA = "os.rejeitada";
    public static final String OS_EXECUCAO_CONCLUIDA = "os.execucao-concluida";
    public static final String OS_PAGA = "os.paga";
    public static final String OS_ENTREGUE = "os.entregue";
    public static final String OS_CANCELADA = "os.cancelada";

    private final OutboxEventoRepository repository;

    @ConsumeEvent(OrdemServicoAberta.TOPICO)
    @Blocking
    @Transactional
    public void onAberta(JsonObject json) {
        gravar(OS_ABERTA, json);
    }

    @ConsumeEvent(OrdemServicoDiagnosticoConcluido.TOPICO)
    @Blocking
    @Transactional
    public void onDiagnosticoConcluido(JsonObject json) {
        gravar(OS_DIAGNOSTICO_CONCLUIDO, json);
    }

    @ConsumeEvent(OrdemServicoAprovada.TOPICO)
    @Blocking
    @Transactional
    public void onAprovada(JsonObject json) {
        gravar(OS_APROVADA, json);
    }

    @ConsumeEvent(OrdemServicoRejeitada.TOPICO)
    @Blocking
    @Transactional
    public void onRejeitada(JsonObject json) {
        gravar(OS_REJEITADA, json);
    }

    @ConsumeEvent(OrdemServicoExecucaoConcluidaEvent.TOPICO)
    @Blocking
    @Transactional
    public void onExecucaoConcluida(JsonObject json) {
        gravar(OS_EXECUCAO_CONCLUIDA, json);
    }

    @ConsumeEvent(OrdemServicoPaga.TOPICO)
    @Blocking
    @Transactional
    public void onPaga(JsonObject json) {
        gravar(OS_PAGA, json);
    }

    @ConsumeEvent(OrdemServicoEntregue.TOPICO)
    @Blocking
    @Transactional
    public void onEntregue(JsonObject json) {
        gravar(OS_ENTREGUE, json);
    }

    @ConsumeEvent(OrdemServicoCancelada.TOPICO)
    @Blocking
    @Transactional
    public void onCancelada(JsonObject json) {
        gravar(OS_CANCELADA, json);
    }

    private void gravar(String routingKey, JsonObject json) {
        OutboxEventoJpaEntity evento = new OutboxEventoJpaEntity();
        evento.setId(UUID.randomUUID());
        evento.setRoutingKey(routingKey);
        evento.setPayload(json.encode());
        evento.setCriadoEm(Instant.now());
        repository.persist(evento);
    }
}
