package br.com.fiap.oficina.api.modules.identidade.api.dto;

import br.com.fiap.oficina.api.modules.identidade.domain.entity.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record CriarUsuarioRequest(
                @NotBlank(message = "O e-mail é obrigatório") @Email(message = "Formato de e-mail inválido") @Schema(description = "E-mail válido do usuário", defaultValue = "mecanico.joao@oficina.com.br") String email,

                @NotBlank(message = "A senha é obrigatória") @Schema(description = "Senha em texto puro (será criptografada)", defaultValue = "SenhaForte123!") String senha,

                @NotNull(message = "O perfil é obrigatório") @Schema(description = "Nível de acesso no sistema", defaultValue = "MECANICO") PerfilUsuario perfil) {
}