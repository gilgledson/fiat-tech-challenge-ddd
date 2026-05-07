package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event;

import io.vertx.core.json.JsonObject;
import java.util.UUID;

public record OrdemServicoCancelada(UUID ordemServicoId, String motivo) {
    public static final String TOPICO = "operacional.ordem-servico.cancelada";

    public JsonObject toJson() {
        return new JsonObject()
                .put("ordemServicoId", ordemServicoId.toString())
                .put("motivo", motivo);
    }
}






