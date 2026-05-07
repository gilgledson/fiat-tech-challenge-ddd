package br.com.fiap.oficina.api.modules.atendimento.cliente.application.usecase;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.dto.ClienteOutput;
import br.com.fiap.oficina.api.modules.atendimento.cliente.application.mapper.ClienteOutputMapper;
import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

import java.util.List;
import java.util.stream.Collectors;

public class ListarClientesUseCaseImpl implements ListarClientesUseCase {

    private final ClienteRepository repository;

    public ListarClientesUseCaseImpl(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Pagina<ClienteOutput> executar(int pagina, int tamanhoLista, boolean incluirInativos) {
        var paginaClientes = repository.listarTodos(pagina, tamanhoLista, incluirInativos);
        List<ClienteOutput> itens = paginaClientes.itens().stream()
                .map(ClienteOutputMapper::toOutput)
                .collect(Collectors.toList());
        return new Pagina<>(itens, paginaClientes.paginaAtual(), paginaClientes.tamanhoPagina(),
                paginaClientes.totalPaginas(), paginaClientes.totalElementos());
    }
}






