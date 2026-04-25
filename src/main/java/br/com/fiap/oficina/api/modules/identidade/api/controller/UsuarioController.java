package br.com.fiap.oficina.api.modules.identidade.api.controller;

import br.com.fiap.oficina.api.modules.identidade.api.dto.CriarUsuarioRequest;
import br.com.fiap.oficina.api.modules.identidade.api.dto.UsuarioResponse;
import br.com.fiap.oficina.api.modules.identidade.application.usecase.CriarUsuarioUseCase;
import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;

@Path("/api/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gestão de identidades e acesso")
public class UsuarioController {
        private final CriarUsuarioUseCase useCase;

        @POST
        @Operation(summary = "Cadastrar novo usuário", description = "Cria um usuário no sistema com senha criptografada.")
        @APIResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class)))
        @APIResponse(responseCode = "400", description = "Dados inválidos ou e-mail já existente")
        public Response criar(@Valid CriarUsuarioRequest request) {

                Usuario usuarioCriado = useCase.executar(
                                request.email(),
                                request.senha(),
                                request.perfil());

                UsuarioResponse response = new UsuarioResponse(
                                usuarioCriado.getId(),
                                usuarioCriado.getEmail(),
                                usuarioCriado.getPerfil(),
                                usuarioCriado.isAtivo());

                return Response.created(URI.create("/api/usuarios/" + response.id()))
                                .entity(response)
                                .build();
        }

}
