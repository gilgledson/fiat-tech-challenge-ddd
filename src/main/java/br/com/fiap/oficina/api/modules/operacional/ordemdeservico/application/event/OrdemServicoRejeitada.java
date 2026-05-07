package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event;

import io.vertx.core.json.JsonObject;
import java.util.UUID;

public record OrdemServicoRejeitada(UUID ordemServicoId) {
    public static final String TOPICO = "operacional.ordem-servico.rejeitada";

    public JsonObject toJson() {
        return new JsonObject()
                .put("ordemServicoId", ordemServicoId.toString());
    }
}






