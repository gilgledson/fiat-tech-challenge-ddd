package br.com.fiap.oficina.api.modules.atendimento.cliente.application.usecase;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;

import java.util.UUID;

public class DeletarClienteUseCaseImpl implements DeletarClienteUseCase {

    private final ClienteRepository repository;

    public DeletarClienteUseCaseImpl(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void executar(UUID id) {
        repository.deletar(id);
    }
}






