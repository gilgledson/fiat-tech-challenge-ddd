package br.com.fiap.oficina.api.modules.atendimento.cliente.api.dto;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.dto.ClienteOutput;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.valueobject.Endereco;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClienteResponse(
        UUID id,
        @JsonProperty("usuario_id")
        UUID usuarioId,
        String nome,
        String email,
        @JsonProperty("cpf_cnpj")
        String cpfCnpj,
        String telefone,
        Endereco endereco,
        @JsonProperty("deletado_em")
        LocalDateTime deletadoEm
) {
    public static ClienteResponse fromOutput(ClienteOutput output) {
        if (output == null) return null;
        return new ClienteResponse(
                output.id(),
                output.usuarioId(),
                output.nome(),
                output.email(),
                output.cpfCnpj(),
                output.telefone(),
                output.endereco(),
                output.deletadoEm()
        );
    }
}






