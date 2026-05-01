package br.com.fiap.oficina.api.modules.faturamento.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.fiap.oficina.api.modules.faturamento.application.repository.FaturaRepository;
import br.com.fiap.oficina.api.modules.faturamento.domain.entity.Fatura;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FaturaRepositoryImpl implements FaturaRepository, PanacheRepositoryBase<FaturaJpaEntity, UUID> {

    @Override
    @Transactional
    public Fatura salvar(Fatura fatura) {
        FaturaJpaEntity entity = FaturaJpaEntity.fromEntity(fatura);
        persist(entity);
        return entity.toEntity();
    }

    @Override
    public Pagina<Fatura> buscarTodos(int pagina, int tamanho, boolean incluirInativos) {
        PanacheQuery<FaturaJpaEntity> query;

        if (incluirInativos) {
            query = findAll();
        } else {
            query = find("deletadoEm is null");
        }

        query.page(pagina, tamanho);
        List<Fatura> faturas = query.list().stream().map(FaturaJpaEntity::toEntity).collect(Collectors.toList());
        long totalElementos = query.count();
        int totalPaginas = (int) Math.ceil((double) totalElementos / tamanho);

        return new Pagina<>(faturas, pagina, tamanho, totalPaginas, totalElementos);
    }

    @Override
    public Optional<Fatura> buscarPorId(UUID id) {
        FaturaJpaEntity entity = findById(id);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(entity.toEntity());
    }

    @Override
    public Optional<Fatura> buscarPorOrdemServicoId(UUID ordemServicoId) {
        return find("ordemServicoId", ordemServicoId).firstResultOptional()
                .map(FaturaJpaEntity::toEntity);
    }

    @Override
    @Transactional
    public Fatura atualizar(Fatura fatura) {
        FaturaJpaEntity entity = FaturaJpaEntity.fromEntity(fatura);
        getEntityManager().merge(entity);
        return entity.toEntity();
    }

    @Override
    @Transactional
    public void remover(UUID id) {
        deleteById(id);
    }

}
