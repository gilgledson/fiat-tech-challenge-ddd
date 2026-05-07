package br.com.fiap.oficina.api.modules.atendimento.cliente.application.mapper;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.dto.ClienteOutput;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.entity.Cliente;

public class ClienteOutputMapper {

    public static ClienteOutput toOutput(Cliente cliente) {
        if (cliente == null) return null;
        
        return new ClienteOutput(
                cliente.getId(),
                cliente.getUsuarioId().orElse(null),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getCpfCnpj(),
                cliente.getTelefone(),
                cliente.getEndereco(),
                cliente.getDeletadoEm().orElse(null)
        );
    }
}






