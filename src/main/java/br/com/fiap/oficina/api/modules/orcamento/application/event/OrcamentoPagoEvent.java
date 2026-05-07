package br.com.fiap.oficina.api.modules.orcamento.application.event;

import io.vertx.core.json.JsonObject;
import java.util.UUID;

public record OrcamentoPagoEvent(UUID orcamentoId, UUID ordemServicoId) {
    public static final String TOPICO = "orcamento.Orcamento.paga";

    public JsonObject toJson() {
        return new JsonObject()
                .put("orcamentoId", orcamentoId.toString())
                .put("ordemServicoId", ordemServicoId.toString());
    }
}






