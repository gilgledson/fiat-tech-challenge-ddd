package br.com.fiap.oficina.api.shared.infrastructure.messaging;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;
import org.jboss.logmanager.MDC;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.listener.SagaEventosListener;
import br.com.fiap.oficina.api.shared.infrastructure.web.CorrelationIdFilter;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.reactive.messaging.rabbitmq.IncomingRabbitMQMetadata;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

/**
 * Consome da fila do OS Service os eventos do Billing Service e do Execution
 * Service e entrega ao listener da Saga conforme a routing key.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class EventosExternosConsumer {

    public static final String ORCAMENTO_ACEITO = "billing.orcamento-aceito";
    public static final String ORCAMENTO_REJEITADO = "billing.orcamento-rejeitado";
    public static final String ORCAMENTO_FALHOU = "billing.orcamento-falhou";
    public static final String PAGAMENTO_CONFIRMADO = "billing.pagamento-confirmado";
    public static final String DIAGNOSTICO_INICIADO = "execucao.diagnostico-iniciado";
    public static final String DIAGNOSTICO_CONCLUIDO = "execucao.diagnostico-concluido";
    public static final String EXECUCAO_INICIADA = "execucao.iniciada";
    public static final String SERVICO_INICIADO = "execucao.servico-iniciado";
    public static final String SERVICO_FINALIZADO = "execucao.servico-finalizado";
    public static final String EXECUCAO_CONCLUIDA = "execucao.concluida";
    public static final String EXECUCAO_FALHOU = "execucao.falhou";

    private static final Logger LOG = Logger.getLogger(EventosExternosConsumer.class);

    private final SagaEventosListener listener;

    @Incoming("eventos-externos")
    @Blocking
    public CompletionStage<Void> consumir(Message<JsonObject> mensagem) {
        Optional<IncomingRabbitMQMetadata> metadata = mensagem.getMetadata(IncomingRabbitMQMetadata.class);
        String routingKey = metadata.map(IncomingRabbitMQMetadata::getRoutingKey).orElse("");
        String correlationId = metadata
                .flatMap(m -> m.getHeader(CorrelationIdFilter.HEADER, String.class))
                .orElse(null);

        if (correlationId != null) {
            MDC.put(CorrelationIdFilter.MDC_KEY, correlationId);
        }
        try {
            despachar(routingKey, mensagem.getPayload());
            return mensagem.ack();
        } catch (RuntimeException e) {
            // nack: a mensagem vai para a fila de mensagens mortas (DLQ) em vez
            // de ser perdida ou reprocessada em loop.
            LOG.errorf(e, "Falha ao processar evento %s", routingKey);
            return mensagem.nack(e);
        } finally {
            MDC.remove(CorrelationIdFilter.MDC_KEY);
        }
    }

    void despachar(String routingKey, JsonObject payload) {
        switch (routingKey) {
            case ORCAMENTO_ACEITO -> listener.onOrcamentoAceito(payload);
            case ORCAMENTO_REJEITADO -> listener.onOrcamentoRejeitado(payload);
            case ORCAMENTO_FALHOU -> listener.onOrcamentoFalhou(payload);
            case PAGAMENTO_CONFIRMADO -> listener.onPagamentoConfirmado(payload);
            case DIAGNOSTICO_INICIADO -> listener.onDiagnosticoIniciado(payload);
            case DIAGNOSTICO_CONCLUIDO -> listener.onDiagnosticoConcluido(payload);
            case EXECUCAO_INICIADA -> listener.onExecucaoIniciada(payload);
            case SERVICO_INICIADO -> listener.onServicoIniciado(payload);
            case SERVICO_FINALIZADO -> listener.onServicoFinalizado(payload);
            case EXECUCAO_CONCLUIDA -> listener.onExecucaoConcluida(payload);
            case EXECUCAO_FALHOU -> listener.onExecucaoFalhou(payload);
            default -> LOG.debugf("Evento %s ignorado pelo OS Service", routingKey);
        }
    }
}
