package br.com.fiap.oficina.api.modules.catalogo.produto.application.repository;

import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProdutoRepository {

    public void salvar(Produto produto);

    public void editar(Produto produto);

    public void delete(UUID id);

    public Optional<Produto> buscarPorId(UUID id);

    public Optional<Produto> buscarPorCodigoBarras(String codigoBarras);

    public List<Produto> buscarPorIds(List<UUID> ids);

    public Pagina<Produto> listarTodos(int pagina, int tamanhoLista, boolean incluirInativos);

}






