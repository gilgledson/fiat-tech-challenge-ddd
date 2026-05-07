package br.com.fiap.oficina.api.modules.identidade.api.dto;

import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CriarUsuarioRequest(
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    String email,

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    String senha,

    @NotNull(message = "O perfil é obrigatório")
    PerfilUsuario perfil
) {}






