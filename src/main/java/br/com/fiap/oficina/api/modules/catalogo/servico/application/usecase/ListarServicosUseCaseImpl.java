package br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase;

import br.com.fiap.oficina.api.modules.catalogo.servico.application.repository.ServicoRepository;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.dto.ServicoOutput;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.mapper.ServicoOutputMapper;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListarServicosUseCaseImpl implements ListarServicosUseCase {

    private final ServicoRepository servicoRepository;
    private final ServicoOutputMapper servicoOutputMapper;

    @Override
    public Pagina<ServicoOutput> executar(int pagina, int tamanho, boolean incluirInativos) {
        var paginaServicos = servicoRepository.listarTodos(pagina, tamanho, incluirInativos);
        var itens = servicoOutputMapper.mapearLista(paginaServicos.itens());
        return new Pagina<>(itens, paginaServicos.paginaAtual(), paginaServicos.tamanhoPagina(),
                paginaServicos.totalPaginas(), paginaServicos.totalElementos());
    }
}






