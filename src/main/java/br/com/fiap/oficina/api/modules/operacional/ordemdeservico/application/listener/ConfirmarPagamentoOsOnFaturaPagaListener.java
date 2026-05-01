package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.listener;

import br.com.fiap.oficina.api.modules.faturamento.application.event.FaturaPagaEvent;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoPaga;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class ConfirmarPagamentoOsOnFaturaPagaListener {

    private final OrdemDeServicoRepository repository;
    private final EventBus eventBus;

    @ConsumeEvent(FaturaPagaEvent.TOPICO)
    @Blocking
    public void onFaturaPaga(JsonObject json) {
        UUID ordemServicoId = UUID.fromString(json.getString("ordemServicoId"));
        UUID faturaId = UUID.fromString(json.getString("faturaId"));

        OrdemDeServico ordem = repository.buscarPorId(ordemServicoId)
                .orElseThrow(
                        () -> new RuntimeException("Ordem de serviço não encontrada para a fatura paga: " + faturaId));

        if (ordem.getStatus() == OrdemDeServicoStatus.AGUARDANDO_PAGAMENTO) {
            ordem.setStatus(OrdemDeServicoStatus.PAGA);
            repository.atualizar(ordem);

            JsonObject payload = new JsonObject().put("ordemServicoId", ordem.getId().toString());
            eventBus.publish(OrdemServicoPaga.TOPICO, payload);

            System.out
                    .println("📢 [Operacional] Status da Ordem de Serviço " + ordem.getId() + " atualizado para PAGA.");
        }
    }
}
