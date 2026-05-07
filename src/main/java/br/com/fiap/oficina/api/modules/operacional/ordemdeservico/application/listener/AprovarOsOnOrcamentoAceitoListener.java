package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.listener;

import br.com.fiap.oficina.api.modules.orcamento.application.event.OrcamentoAceitoEvent;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto.AprovarServicoRequest;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado.AprovarOrdemDeServicoUseCase;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class AprovarOsOnOrcamentoAceitoListener {

    private final AprovarOrdemDeServicoUseCase aprovarUseCase;

    @ConsumeEvent(OrcamentoAceitoEvent.TOPICO)
    @Blocking
    public void onOrcamentoAceito(JsonObject json) {
        UUID ordemServicoId = UUID.fromString(json.getString("ordemServicoId"));

        List<UUID> servicosAceitosIds = json.getJsonArray("servicosAceitos").stream()
                .map(obj -> UUID.fromString(obj.toString()))
                .collect(Collectors.toList());

        List<UUID> servicosRejeitadosIds = json.getJsonArray("servicosRejeitados").stream()
                .map(obj -> UUID.fromString(obj.toString()))
                .collect(Collectors.toList());

        if (!servicosAceitosIds.isEmpty() || !servicosRejeitadosIds.isEmpty()) {
            AprovarServicoRequest request = new AprovarServicoRequest(servicosAceitosIds, servicosRejeitadosIds);
            aprovarUseCase.executar(ordemServicoId, request);

            System.out.println("📢 [Operacional] Ordem de Serviço " + ordemServicoId +
                    " processada via aceite manual: " + servicosAceitosIds.size() + " aprovados, " +
                    servicosRejeitadosIds.size() + " rejeitados.");
        }
    }
}
