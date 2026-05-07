package br.com.fiap.oficina.api.modules.orcamento.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.fiap.oficina.api.modules.orcamento.application.repository.OrcamentoRepository;
import br.com.fiap.oficina.api.modules.orcamento.domain.entity.Orcamento;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class OrcamentoRepositoryImpl implements OrcamentoRepository, PanacheRepositoryBase<OrcamentoJpaEntity, UUID> {

    @Override
    @Transactional
    public Orcamento salvar(Orcamento orcamento) {
        OrcamentoJpaEntity entity = OrcamentoJpaEntity.fromEntity(orcamento);
        persist(entity);
        return entity.toEntity();
    }

    @Override
    public Pagina<Orcamento> buscarTodos(int pagina, int tamanho, boolean incluirInativos) {
        PanacheQuery<OrcamentoJpaEntity> query;

        if (incluirInativos) {
            query = findAll();
        } else {
            query = find("deletadoEm is null");
        }

        query.page(pagina, tamanho);
        List<Orcamento> orcamentos = query.list().stream().map(OrcamentoJpaEntity::toEntity).collect(Collectors.toList());
        long totalElementos = query.count();
        int totalPaginas = (int) Math.ceil((double) totalElementos / tamanho);

        return new Pagina<>(orcamentos, pagina, tamanho, totalPaginas, totalElementos);
    }

    @Override
    public Optional<Orcamento> buscarPorId(UUID id) {
        OrcamentoJpaEntity entity = findById(id);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(entity.toEntity());
    }

    @Override
    public Optional<Orcamento> buscarPorOrdemServicoId(UUID ordemServicoId) {
        return find("ordemServicoId", ordemServicoId).firstResultOptional()
                .map(OrcamentoJpaEntity::toEntity);
    }

    @Override
    @Transactional
    public Orcamento atualizar(Orcamento orcamento) {
        OrcamentoJpaEntity entity = OrcamentoJpaEntity.fromEntity(orcamento);
        getEntityManager().merge(entity);
        return entity.toEntity();
    }

    @Override
    @Transactional
    public void remover(UUID id) {
        deleteById(id);
    }

}







