package br.com.fiap.oficina.api.modules.identidade.api.controller;

import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import br.com.fiap.oficina.api.modules.identidade.api.dto.AtualizarTokenRequest;
import br.com.fiap.oficina.api.modules.identidade.api.dto.CriarUsuarioRequest;
import br.com.fiap.oficina.api.modules.identidade.api.dto.LoginRequest;
import br.com.fiap.oficina.api.modules.identidade.api.dto.TokenResponse;
import br.com.fiap.oficina.api.modules.identidade.api.dto.UsuarioResponse;
import br.com.fiap.oficina.api.modules.identidade.application.usecase.comandos.CriarUsuarioUseCase;
import br.com.fiap.oficina.api.modules.identidade.application.usecase.consulta.AtualizarTokenUseCase;
import br.com.fiap.oficina.api.modules.identidade.application.usecase.consulta.EfetuarLoginUseCase;
import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
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
        private final AtualizarTokenUseCase refreshTokenUseCase;
        private final EfetuarLoginUseCase loginUseCase;

        @Path("/login")
        @POST
        @PermitAll
        @Operation(summary = "Login", description = "Autentica usuário e retorna tokens JWT")
        @APIResponse(responseCode = "200", description = "Login realizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class)))
        @APIResponse(responseCode = "401", description = "Credenciais inválidas")
        public Response login(@Valid LoginRequest credenciais) {
                TokenResponse response = loginUseCase.executar(credenciais.email(), credenciais.senha());
                return Response.ok(response).build();
        }

        @Path("/refresh")
        @POST
        @PermitAll
        @Operation(summary = "Refresh Token", description = "Gera novos tokens JWT usando o Refresh Token")
        @APIResponse(responseCode = "200", description = "Tokens renovados com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class)))
        @APIResponse(responseCode = "401", description = "Refresh Token inválido")
        public Response refresh(@Valid AtualizarTokenRequest request) {
                TokenResponse response = refreshTokenUseCase.executar(request.refreshToken());
                return Response.ok(response).build();
        }

        @POST
        @RolesAllowed(PerfilUsuario.Constants.ADMIN)
        @jakarta.transaction.Transactional
        @Operation(summary = "Cadastrar novo usuário", description = "Cria um usuário no sistema com senha criptografada.")
        @APIResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class)))
        @APIResponse(responseCode = "400", description = "Dados inválidos ou e-mail já existente")
        public Response criar(@Valid CriarUsuarioRequest request) {

                Usuario usuarioCriado = useCase.executar(
                                request.email(),
                                request.senha(),
                                request.perfil());

                UsuarioResponse response = UsuarioResponse.fromDomain(usuarioCriado);

                return Response.created(URI.create("/api/usuarios/" + response.id()))
                                .entity(response)
                                .build();
        }

}






