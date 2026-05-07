package br.com.fiap.oficina.api.modules.atendimento.cliente.application.dto;

import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.valueobject.Endereco;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClienteOutput(
        UUID id,
        UUID usuarioId,
        String nome,
        String email,
        String cpfCnpj,
        String telefone,
        Endereco endereco,
        LocalDateTime deletadoEm
) {
}






