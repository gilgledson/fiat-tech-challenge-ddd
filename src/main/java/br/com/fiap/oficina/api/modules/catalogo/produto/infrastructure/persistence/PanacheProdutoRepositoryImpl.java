package br.com.fiap.oficina.api.modules.catalogo.produto.infrastructure.persistence;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

@ApplicationScoped
public class PanacheProdutoRepositoryImpl implements
        ProdutoRepository, PanacheRepositoryBase<ProdutoJpaEntity, UUID> {
    @Override
    @Transactional
    public void salvar(Produto produto) {
        ProdutoJpaEntity entity = new ProdutoJpaEntity();
        entity.setId(produto.getId());
        entity.setNome(produto.getNome());
        entity.setCodigoBarras(produto.getCodigoBarras());
        entity.setPrecoUnitario(produto.getPrecoUnitario());
        entity.setQuantidadeEstoqueFisico(produto.getQuantidadeEstoqueFisico());
        entity.setQuantidadeEstoqueReservado(produto.getQuantidadeEstoqueReservado());
        entity.setUnidadeMedida(produto.getUnidadeMedida());

        persist(entity);
    }

    @Override
    @Transactional
    public void editar(Produto produto) {
        final ProdutoJpaEntity entity = findByIdOptional(produto.getId())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        entity.setNome(produto.getNome());
        entity.setCodigoBarras(produto.getCodigoBarras());
        entity.setPrecoUnitario(produto.getPrecoUnitario());
        entity.setQuantidadeEstoqueFisico(produto.getQuantidadeEstoqueFisico());
        entity.setQuantidadeEstoqueReservado(produto.getQuantidadeEstoqueReservado());
        entity.setUnidadeMedida(produto.getUnidadeMedida());
        entity.setDeletadoEm(
                produto.getDeletadoEm().isPresent() ? produto.getDeletadoEm().get() : null);
        
        flush();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        deleteById(id);
    }

    @Override
    public Optional<Produto> buscarPorId(UUID id) {
        return findByIdOptional(id).map(this::converterParaDominio);
    }

    @Override
    public Optional<Produto> buscarPorCodigoBarras(String codigoBarras) {
        return find("codigoBarras", codigoBarras).firstResultOptional()
                .map(this::converterParaDominio);
    }

    @Override
    public List<Produto> buscarPorIds(List<UUID> ids) {

        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        return find("id in ?1", ids)
                .stream()
                .map(this::converterParaDominio)
                .toList();
    }

    @Override
    public Pagina<Produto> listarTodos(int pagina, int tamanhoLista, boolean incluirInativos) {
        var query = incluirInativos ? findAll() : find("deletadoEm is null");
        query.page(Page.of(pagina, tamanhoLista));
        List<Produto> itens = query
                .stream()
                .map(this::converterParaDominio)
                .collect(Collectors.toList());
        return new Pagina<>(itens, pagina, tamanhoLista, (int) query.pageCount(), query.count());
    }

    private Produto converterParaDominio(ProdutoJpaEntity entity) {
        return Produto.reconstituir(
                entity.getId(),
                entity.getNome(),
                entity.getCodigoBarras(),
                entity.getPrecoUnitario(),
                entity.getQuantidadeEstoqueFisico(),
                entity.getQuantidadeEstoqueReservado(),
                entity.getUnidadeMedida(),
                Optional.ofNullable(entity.getDeletadoEm()));
    }
}






