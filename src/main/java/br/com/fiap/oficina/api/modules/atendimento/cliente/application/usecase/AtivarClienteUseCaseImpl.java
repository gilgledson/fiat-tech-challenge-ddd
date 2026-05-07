package br.com.fiap.oficina.api.modules.atendimento.cliente.application.usecase;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.entity.Cliente;
import jakarta.ws.rs.NotFoundException;

import java.util.UUID;

public class AtivarClienteUseCaseImpl implements AtivarClienteUseCase {

    private final ClienteRepository repository;

    public AtivarClienteUseCaseImpl(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void executar(UUID id) {
        Cliente cliente = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

        cliente.ativar();
        repository.atualizar(cliente);
    }
}






