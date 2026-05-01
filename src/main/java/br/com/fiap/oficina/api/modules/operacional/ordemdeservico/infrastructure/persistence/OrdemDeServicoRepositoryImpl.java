package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.infrastructure.persistence;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class OrdemDeServicoRepositoryImpl
        implements OrdemDeServicoRepository, PanacheRepositoryBase<OrdemDeServicoJpaEntity, UUID> {

    @Override
    public Pagina<OrdemDeServico> buscarTodas(UUID clienteId, UUID veiculoId, int pagina, int tamanho,
            boolean incluirInativas) {
        List<String> condicoes = new ArrayList<>();
        Map<String, Object> parametros = new HashMap<>();

        if (clienteId != null) {
            condicoes.add("clienteId = :clienteId");
            parametros.put("clienteId", clienteId);
        }

        if (veiculoId != null) {
            condicoes.add("veiculoId = :veiculoId");
            parametros.put("veiculoId", veiculoId);
        }

        if (!incluirInativas) {
            condicoes.add("deletadoEm is null");
        }

        String hql = String.join(" and ", condicoes);

        PanacheQuery<OrdemDeServicoJpaEntity> query = hql.isEmpty() ? findAll() : find(hql, parametros);

        query.page(pagina, tamanho);

        List<OrdemDeServico> itens = query
                .list()
                .stream()
                .map(OrdemDeServicoJpaEntity::toDomain)
                .toList();

        return new Pagina<>(itens, pagina, tamanho, (int) query.pageCount(), query.count());

    }

    @Override
    @Transactional
    public void salvar(OrdemDeServico ordem) {
        OrdemDeServicoJpaEntity servicoJpa = OrdemDeServicoJpaEntity.fromDomain(ordem);
        persist(servicoJpa);
    }

    @Override
    @Transactional
    public void atualizar(OrdemDeServico ordem) {
        OrdemDeServicoJpaEntity entity = findById(ordem.getId());

        if (entity != null) {
            entity.setStatus(ordem.getStatus().name());
            entity.setDataInicioExecucao(ordem.getDataInicioExecucao());
            entity.setDataFimExecucao(ordem.getDataFimExecucao());
            entity.getProdutos().clear();
            entity.getServicos().clear();

            List<OrdemDeServicoProdutosEmbeddable> novosProdutos = ordem.getProdutos().stream()
                    .map(OrdemDeServicoProdutosEmbeddable::fromDomain)
                    .toList();

            List<OrdemDeServicoServicosEmbeddable> novosServicos = ordem.getServicos().stream()
                    .map(OrdemDeServicoServicosEmbeddable::fromDomain)
                    .toList();

            entity.getProdutos().addAll(novosProdutos);
            entity.getServicos().addAll(novosServicos);
        }
    }

    @Override
    @Transactional
    public void deletar(UUID id) {
        deleteById(id);
    }

    @Override
    @Transactional
    public Optional<OrdemDeServico> buscarPorId(UUID id) {
        OrdemDeServicoJpaEntity ordem = findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        return Optional.of(ordem.toDomain());
    }
}