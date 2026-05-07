package br.com.fiap.oficina.api.modules.orcamento.application.repository;

import java.util.Optional;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.orcamento.domain.entity.Orcamento;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

public interface OrcamentoRepository {
    public Orcamento salvar(Orcamento orcamento);

    public Pagina<Orcamento> buscarTodos(int pagina, int tamanho, boolean incluirInativos);

    public Optional<Orcamento> buscarPorId(UUID id);
    
    public Optional<Orcamento> buscarPorOrdemServicoId(UUID ordemServicoId);

    public Orcamento atualizar(Orcamento orcamento);

    public void remover(UUID id);
}







