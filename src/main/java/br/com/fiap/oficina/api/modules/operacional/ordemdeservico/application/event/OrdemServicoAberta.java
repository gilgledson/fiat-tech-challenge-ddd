package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event;

import io.vertx.core.json.JsonObject;
import java.util.UUID;

public record OrdemServicoAberta(UUID ordemServicoId, UUID clienteId, UUID veiculoId) {
    public static final String TOPICO = "operacional.ordem-servico.aberta";

    public JsonObject toJson() {
        return new JsonObject()
                .put("ordemServicoId", ordemServicoId.toString())
                .put("clienteId", clienteId.toString())
                .put("veiculoId", veiculoId.toString());
    }
}






