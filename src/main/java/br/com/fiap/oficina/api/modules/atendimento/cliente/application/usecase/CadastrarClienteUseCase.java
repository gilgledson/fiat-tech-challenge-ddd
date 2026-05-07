package br.com.fiap.oficina.api.modules.atendimento.cliente.application.usecase;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.dto.ClienteOutput;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.valueobject.Endereco;

import java.util.UUID;

public interface CadastrarClienteUseCase {
    ClienteOutput executar(UUID usuarioId, String nome, String email, String cpfCnpj, String telefone,
            Endereco endereco);
}






