package br.com.fiap.oficina.api.modules.atendimento.cliente.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.UUID;

public record ClienteRequest(
                @Schema(description = "ID do usuário associado", defaultValue = "123e4567-e89b-12d3-a456-426614174000") @JsonProperty("usuario_id") UUID usuarioId,

                @NotBlank(message = "O nome é obrigatório") @Schema(description = "Nome do cliente", defaultValue = "João da Silva") String nome,

                @NotBlank(message = "O e-mail é obrigatório") @Schema(description = "E-mail do cliente", defaultValue = "joao@email.com") String email,

                @NotBlank(message = "O CPF/CNPJ é obrigatório") @JsonProperty("cpf_cnpj") @Schema(description = "CPF ou CNPJ do cliente", defaultValue = "12345678901") String cpfCnpj,

                @NotBlank(message = "O telefone é obrigatório") @Schema(description = "Telefone de contato", defaultValue = "11999999999") String telefone,

                @NotNull(message = "O endereço é obrigatório") @jakarta.validation.Valid @Schema(description = "Endereço do cliente") EnderecoRequest endereco) {
}






