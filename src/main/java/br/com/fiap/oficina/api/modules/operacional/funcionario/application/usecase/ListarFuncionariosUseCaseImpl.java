package br.com.fiap.oficina.api.modules.operacional.funcionario.application.usecase;

import br.com.fiap.oficina.api.modules.operacional.funcionario.application.dto.FuncionarioOutput;
import br.com.fiap.oficina.api.modules.operacional.funcionario.application.mapper.FuncionarioOutputMapper;
import br.com.fiap.oficina.api.modules.operacional.funcionario.application.repository.FuncionarioRepository;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;
import java.util.List;

public class ListarFuncionariosUseCaseImpl implements ListarFuncionariosUseCase {

    private final FuncionarioRepository repository;

    public ListarFuncionariosUseCaseImpl(FuncionarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Pagina<FuncionarioOutput> executar(int pagina, int tamanhoLista, boolean incluirInativos) {
        Pagina<br.com.fiap.oficina.api.modules.operacional.funcionario.domain.entity.Funcionario> paginaDominio = 
                repository.listarTodos(pagina, tamanhoLista, incluirInativos);
        
        List<FuncionarioOutput> itensOutput = paginaDominio.itens().stream()
                .map(FuncionarioOutputMapper::toOutput)
                .toList();

        return new Pagina<>(
                itensOutput,
                paginaDominio.paginaAtual(),
                paginaDominio.tamanhoPagina(),
                paginaDominio.totalPaginas(),
                paginaDominio.totalElementos()
        );
    }
}






