package br.com.fiap.oficina.api.modules.catalogo.servico.application.repository;

import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.Servico;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

import java.util.Optional;
import java.util.UUID;

public interface ServicoRepository {
    public void salvar(Servico servico);

    Pagina<Servico> listarTodos(int pagina, int tamanho, boolean incluirInativos);

    public Optional<Servico> buscarPorId(UUID id);

    public void atualizar(Servico servico);

    public void deletar(UUID id);
}






