package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event;

import io.vertx.core.json.JsonObject;
import java.util.UUID;

public record OrdemServicoEntregue(UUID ordemServicoId, UUID clienteId) {
    public static final String TOPICO = "operacional.ordem-servico.entregue";

    public JsonObject toJson() {
        return new JsonObject()
                .put("ordemServicoId", ordemServicoId.toString())
                .put("clienteId", clienteId.toString());
    }
}






