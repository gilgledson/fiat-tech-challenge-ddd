package br.com.fiap.oficina.api.modules.atendimento.cliente.application.usecase;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.dto.ClienteOutput;
import br.com.fiap.oficina.api.modules.atendimento.cliente.application.mapper.ClienteOutputMapper;
import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.entity.Cliente;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.valueobject.Endereco;
import jakarta.ws.rs.NotFoundException;

import java.util.UUID;

public class EditarClienteUseCaseImpl implements EditarClienteUseCase {

    private final ClienteRepository repository;

    public EditarClienteUseCaseImpl(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public ClienteOutput executar(UUID id, String nome, String email, String telefone, Endereco endereco) {
        Cliente cliente = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

        cliente.atualizarDados(nome, email, telefone, endereco);
        repository.atualizar(cliente);

        return ClienteOutputMapper.toOutput(cliente);
    }
}






