package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.listener;

import br.com.fiap.oficina.api.modules.orcamento.application.event.OrcamentoRejeitadoEvent;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado.RejeitarOrcamentoUseCase;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class RejeitarOsOnOrcamentoRejeitadoListener {

    private final RejeitarOrcamentoUseCase rejeitarUseCase;

    @ConsumeEvent(OrcamentoRejeitadoEvent.TOPICO)
    @Blocking
    public void onOrcamentoRejeitado(JsonObject json) {
        UUID ordemServicoId = UUID.fromString(json.getString("ordemServicoId"));

        rejeitarUseCase.executar(ordemServicoId);

        System.out.println("📢 [Operacional] Ordem de Serviço " + ordemServicoId + 
                " rejeitada automaticamente via recusa de orçamento.");
    }
}
