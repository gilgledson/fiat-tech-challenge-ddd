package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event;

import io.vertx.core.json.JsonObject;
import java.util.UUID;

public record OrdemServicoServicoAdicionado(UUID ordemServicoId, UUID clienteId, UUID servicoId, String nomeServico) {
    public static final String TOPICO = "operacional.ordem-servico.servico-adicionado";

    public JsonObject toJson() {
        return new JsonObject()
                .put("ordemServicoId", ordemServicoId.toString())
                .put("clienteId", clienteId.toString())
                .put("servicoId", servicoId.toString())
                .put("nomeServico", nomeServico);
    }
}
