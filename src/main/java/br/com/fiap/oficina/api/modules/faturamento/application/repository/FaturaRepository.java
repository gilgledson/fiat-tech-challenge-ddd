package br.com.fiap.oficina.api.modules.faturamento.application.repository;

import java.util.Optional;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.faturamento.domain.entity.Fatura;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

public interface FaturaRepository {
    public Fatura salvar(Fatura fatura);

    public Pagina<Fatura> buscarTodos(int pagina, int tamanho, boolean incluirInativos);

    public Optional<Fatura> buscarPorId(UUID id);
    
    public Optional<Fatura> buscarPorOrdemServicoId(UUID ordemServicoId);

    public Fatura atualizar(Fatura fatura);

    public void remover(UUID id);
}
