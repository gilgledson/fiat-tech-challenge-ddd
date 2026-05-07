package br.com.fiap.oficina.api.modules.orcamento.application.event;

import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record OrcamentoAceitoEvent(UUID orcamentoId, UUID ordemServicoId, List<UUID> servicosAceitos, List<UUID> servicosRejeitados) {
    public static final String TOPICO = "orcamento.aceito";

    public JsonObject toJson() {
        return new JsonObject()
                .put("orcamentoId", orcamentoId.toString())
                .put("ordemServicoId", ordemServicoId.toString())
                .put("servicosAceitos", servicosAceitos.stream().map(UUID::toString).collect(Collectors.toList()))
                .put("servicosRejeitados", servicosRejeitados.stream().map(UUID::toString).collect(Collectors.toList()));
    }
}






