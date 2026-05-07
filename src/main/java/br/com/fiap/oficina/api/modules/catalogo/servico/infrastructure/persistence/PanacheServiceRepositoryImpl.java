package br.com.fiap.oficina.api.modules.catalogo.servico.infrastructure.persistence;

import br.com.fiap.oficina.api.modules.catalogo.servico.application.repository.ServicoRepository;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.ProdutoSugerido;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.Servico;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

@ApplicationScoped
public class PanacheServiceRepositoryImpl implements ServicoRepository, PanacheRepositoryBase<ServicoJpaEntity, UUID> {

    @Override
    @Transactional
    public void salvar(Servico servico) {
        ServicoJpaEntity servicoJpa = new ServicoJpaEntity();
        servicoJpa.setId(servico.getId());
        servicoJpa.setNome(servico.getNome());
        servicoJpa.setTipo(servico.getTipo());
        servicoJpa.setPrecoBase(servico.getPrecoBase());

        List<ProdutoSugeridoEmbeddable> produtos = servico.getProdutosSugeridos().stream()
                .map((e) -> new ProdutoSugeridoEmbeddable(e.produtoId(), e.quantidade()))
                .toList();

        servicoJpa.setProdutosSugeridos(produtos);

        persist(servicoJpa);
    }

    @Override
    public Pagina<Servico> listarTodos(int pagina, int tamanho, boolean incluirInativos) {
        PanacheQuery<ServicoJpaEntity> query;

        if (incluirInativos) {
            query = findAll();
        } else {
            query = find("deletadoEm is null");
        }

        query.page(pagina, tamanho);
        List<Servico> itens = query
                .list()
                .stream()
                .map(this::mapJpaParaDominio)
                .toList();
        return new Pagina<>(itens, pagina, tamanho, (int) query.pageCount(), query.count());
    }

    @Override
    public Optional<Servico> buscarPorId(UUID id) {
        return findByIdOptional(id).map(this::mapJpaParaDominio);
    }

    @Override
    @Transactional
    public void atualizar(Servico servico) {
        ServicoJpaEntity entity = findById(servico.getId());

        if (entity != null) {
            entity.setNome(servico.getNome());
            entity.setTipo(servico.getTipo());
            entity.setPrecoBase(servico.getPrecoBase());
            entity.setDeletadoEm(servico.getDeletadoEm());
            entity.getProdutosSugeridos().clear();

            List<ProdutoSugeridoEmbeddable> novosProdutos = servico.getProdutosSugeridos().stream()
                    .map(e -> new ProdutoSugeridoEmbeddable(e.produtoId(), e.quantidade()))
                    .toList();

            entity.getProdutosSugeridos().addAll(novosProdutos);
        }
    }

    @Override
    @Transactional
    public void deletar(UUID id) {
        deleteById(id);
    }

    private Servico mapJpaParaDominio(ServicoJpaEntity entity) {
        List<ProdutoSugerido> produtos = entity.getProdutosSugeridos().stream()
                .map(emb -> new ProdutoSugerido(emb.getProdutoId(), emb.getQuantidade()))
                .toList();

        return Servico.reconstituir(
                entity.getId(),
                entity.getNome(),
                entity.getTipo(),
                entity.getPrecoBase(),
                entity.getDeletadoEm(),
                produtos);
    }
}






