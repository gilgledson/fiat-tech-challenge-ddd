package br.com.fiap.oficina.api.modules.relatorios.api.controller;

import java.util.List;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import br.com.fiap.oficina.api.modules.relatorios.api.dto.RelatorioTempoMedioServicoResponse;
import br.com.fiap.oficina.api.modules.relatorios.application.usecase.consultas.ObterRelatorioEsforcoOsUseCase;
import br.com.fiap.oficina.api.modules.relatorios.application.usecase.consultas.ObterRelatorioTempoMedioServicoUseCase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/relatorios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Relatórios", description = "Endpoints para extração de métricas e relatórios operacionais")
@ApplicationScoped
@RequiredArgsConstructor
public class RelatorioController {

    private final ObterRelatorioEsforcoOsUseCase obterEsforcoOsUseCase;
    private final ObterRelatorioTempoMedioServicoUseCase obterTempoMedioServicoUseCase;

    @GET
    @Path("/esforco-os/{osId}")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.MECANICO })
    @Operation(summary = "Obter esforço total de uma OS", description = "Retorna o total de serviços realizados e o tempo total de esforço em minutos.")
    public Response obterEsforcoPorOs(@PathParam("osId") UUID osId) {
        return obterEsforcoOsUseCase.executar(osId)
                .map(r -> Response.ok(r).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/tempo-medio-servicos")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN })
    @Operation(summary = "Obter tempo médio de execução por serviço", description = "Retorna a média histórica de tempo de execução para cada tipo de serviço do catálogo.")
    public List<RelatorioTempoMedioServicoResponse> obterTemposMedios() {
        return obterTempoMedioServicoUseCase.executar();
    }
}
