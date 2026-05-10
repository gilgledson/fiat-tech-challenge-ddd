package br.com.fiap.oficina.api.modules.orcamento.application.event;

import io.vertx.core.json.JsonObject;
import java.util.UUID;

public record OrcamentoRejeitadoEvent(UUID orcamentoId, UUID ordemServicoId) {
    public static final String TOPICO = "orcamento.rejeitado";

    public JsonObject toJson() {
        return new JsonObject()
                .put("orcamentoId", orcamentoId.toString())
                .put("ordemServicoId", ordemServicoId.toString());
    }
}
