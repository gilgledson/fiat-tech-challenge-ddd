package br.com.fiap.oficina.api.modules.faturamento.application.event;

import io.vertx.core.json.JsonObject;
import java.util.UUID;

public record FaturaPagaEvent(UUID faturaId, UUID ordemServicoId) {
    public static final String TOPICO = "faturamento.fatura.paga";

    public JsonObject toJson() {
        return new JsonObject()
                .put("faturaId", faturaId.toString())
                .put("ordemServicoId", ordemServicoId.toString());
    }
}
