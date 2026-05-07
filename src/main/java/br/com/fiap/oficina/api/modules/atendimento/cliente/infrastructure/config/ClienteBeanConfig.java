package br.com.fiap.oficina.api.modules.atendimento.cliente.infrastructure.config;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.atendimento.cliente.application.usecase.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ClienteBeanConfig {

    @Produces
    public CadastrarClienteUseCase cadastrarClienteUseCase(ClienteRepository repository) {
        return new CadastrarClienteUseCaseImpl(repository);
    }

    @Produces
    public EditarClienteUseCase editarClienteUseCase(ClienteRepository repository) {
        return new EditarClienteUseCaseImpl(repository);
    }

    @Produces
    public DeletarClienteUseCase deletarClienteUseCase(ClienteRepository repository) {
        return new DeletarClienteUseCaseImpl(repository);
    }

    @Produces
    public AtivarClienteUseCase ativarClienteUseCase(ClienteRepository repository) {
        return new AtivarClienteUseCaseImpl(repository);
    }

    @Produces
    public InativarClienteUseCase inativarClienteUseCase(ClienteRepository repository) {
        return new InativarClienteUseCaseImpl(repository);
    }

    @Produces
    public ListarClientesUseCase listarClientesUseCase(ClienteRepository repository) {
        return new ListarClientesUseCaseImpl(repository);
    }
}






