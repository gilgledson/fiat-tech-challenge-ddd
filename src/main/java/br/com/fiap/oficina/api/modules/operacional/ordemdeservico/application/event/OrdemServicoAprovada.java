package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event;

import java.util.List;
import java.util.UUID;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public record OrdemServicoAprovada(UUID ordemServicoId, List<ServicoAprovado> servicos) {

    public static final String TOPICO = "operacional.ordem-servico.aprovada";

    /** Serviço aceito pelo cliente: item da OS, serviço do catálogo e nome. */
    public record ServicoAprovado(UUID id, UUID servicoId, String nome) {
    }

    public JsonObject toJson() {
        JsonArray itens = new JsonArray();
        servicos.forEach(s -> itens.add(new JsonObject()
                .put("id", s.id().toString())
                .put("servicoId", s.servicoId().toString())
                .put("nome", s.nome())));
        return new JsonObject()
                .put("ordemServicoId", ordemServicoId.toString())
                .put("servicos", itens);
    }
}
