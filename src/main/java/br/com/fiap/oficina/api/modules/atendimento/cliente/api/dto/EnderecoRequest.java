package br.com.fiap.oficina.api.modules.atendimento.cliente.api.dto;

import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record EnderecoRequest(
        @NotBlank(message = "O CEP é obrigatório") @Schema(description = "CEP do cliente", defaultValue = "01001-000") String cep,

        @NotBlank(message = "O logradouro é obrigatório") @Schema(description = "Logradouro (Rua, Avenida, etc)", defaultValue = "Praça da Sé") String logradouro,

        @NotBlank(message = "O número é obrigatório") @Schema(description = "Número do endereço", defaultValue = "s/n") String numero,

        @Schema(description = "Complemento do endereço", defaultValue = "Lado ímpar") String complemento,

        @NotBlank(message = "O bairro é obrigatório") @Schema(description = "Bairro", defaultValue = "Sé") String bairro,

        @NotBlank(message = "A cidade é obrigatória") @Schema(description = "Cidade", defaultValue = "São Paulo") String cidade,

        @NotBlank(message = "O estado (UF) é obrigatório") @Schema(description = "Sigla do estado (UF)", defaultValue = "SP") String estado) {
    public br.com.fiap.oficina.api.modules.atendimento.cliente.domain.valueobject.Endereco toDomain() {
        return new br.com.fiap.oficina.api.modules.atendimento.cliente.domain.valueobject.Endereco(
                this.cep(),
                this.logradouro(),
                this.numero(),
                this.complemento(),
                this.bairro(),
                this.cidade(),
                this.estado());
    }
}






