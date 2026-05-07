package br.com.fiap.oficina.api.modules.identidade.api.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
                @NotBlank(message = "O e-mail é obrigatório") @Email(message = "Formato de e-mail inválido") @Schema(description = "E-mail válido do usuário", defaultValue = "mecanico.joao@oficina.com.br") String email,

                @NotBlank(message = "A senha é obrigatória") @Schema(description = "Senha em texto puro (será criptografada)", defaultValue = "SenhaForte123!") String senha

) {
}






