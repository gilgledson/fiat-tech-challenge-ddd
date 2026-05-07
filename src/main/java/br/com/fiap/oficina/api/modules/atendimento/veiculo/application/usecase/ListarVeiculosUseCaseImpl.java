package br.com.fiap.oficina.api.modules.atendimento.veiculo.application.usecase;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.dto.VeiculoOutput;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.mapper.VeiculoOutputMapper;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

import java.util.List;
import java.util.stream.Collectors;

public class ListarVeiculosUseCaseImpl implements ListarVeiculosUseCase {

    private final VeiculoRepository repository;

    public ListarVeiculosUseCaseImpl(VeiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Pagina<VeiculoOutput> executar(int pagina, int tamanhoLista, boolean incluirInativos) {
        var paginaVeiculos = repository.listarTodos(pagina, tamanhoLista, incluirInativos);
        List<VeiculoOutput> itens = paginaVeiculos.itens().stream()
                .map(VeiculoOutputMapper::toOutput)
                .collect(Collectors.toList());
        return new Pagina<>(itens, paginaVeiculos.paginaAtual(), paginaVeiculos.tamanhoPagina(),
                paginaVeiculos.totalPaginas(), paginaVeiculos.totalElementos());
    }
}






