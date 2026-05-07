package br.com.fiap.oficina.api.modules.atendimento.cliente.application.usecase;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.dto.ClienteOutput;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.valueobject.Endereco;

import java.util.UUID;

public interface EditarClienteUseCase {
    ClienteOutput executar(UUID id, String nome, String email, String telefone, Endereco endereco);
}






