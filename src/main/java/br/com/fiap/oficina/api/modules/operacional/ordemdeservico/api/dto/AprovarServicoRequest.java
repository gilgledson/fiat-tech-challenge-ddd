package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

public record AprovarServicoRequest(
        @Schema(description = "Lista de IDs de serviços que foram aprovados pelo cliente") List<UUID> servicosAprovados,

        @Schema(description = "Lista de IDs de serviços que foram rejeitados pelo cliente") List<UUID> servicosRejeitados) {
}






