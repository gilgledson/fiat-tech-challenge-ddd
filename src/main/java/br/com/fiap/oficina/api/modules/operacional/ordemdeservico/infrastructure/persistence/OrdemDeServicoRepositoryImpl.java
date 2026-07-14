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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

        // A listagem padrão de OS não mostra ordens já finalizadas (pagas) ou entregues,
        // nem os caminhos alternativos (rejeitada/cancelada) — só o que ainda está em
        // andamento no fluxo operacional.
        condicoes.add("status not in ('PAGA', 'ENTREGUE', 'REJEITADA', 'CANCELADA')");

        String hql = String.join(" and ", condicoes) +
                " order by case status" +
                " when 'EM_EXECUCAO' then 1" +
                " when 'APROVADA' then 2" +
                " when 'AGUARDANDO_APROVACAO' then 3" +
                " when 'EM_DIAGNOSTICO' then 4" +
                " when 'ABERTA' then 5" +
                " else 6 end, dataAbertura asc";

        PanacheQuery<OrdemDeServicoJpaEntity> query = find(hql, parametros);

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

            java.util.Set<UUID> idsServicosDomain = ordem.getServicos().stream()
                    .map(servico -> servico.getId())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            entity.getServicos().removeIf(
                    servicoJpa -> servicoJpa.getId() != null && !idsServicosDomain.contains(servicoJpa.getId()));

            for (var servicoDomain : ordem.getServicos()) {
                Optional<OrdemDeServicoServicosJpaEntity> servicoOpt = entity.getServicos().stream()
                        .filter(s -> s.getId() != null && s.getId().equals(servicoDomain.getId()))
                        .findFirst();

                if (servicoOpt.isEmpty()) {
                    entity.getServicos().add(OrdemDeServicoServicosJpaEntity.fromDomain(servicoDomain));
                } else {
                    OrdemDeServicoServicosJpaEntity servicoEntity = servicoOpt.get();
                    servicoEntity.setStatus(servicoDomain.getStatus().name());
                    servicoEntity.setDataInicioExecucao(servicoDomain.getDataInicioExecucao());
                    servicoEntity.setDataFimExecucao(servicoDomain.getDataFimExecucao());
                    servicoEntity.setUsuarioExecutorId(servicoDomain.getUsuarioExecutorId());

                    java.util.Set<UUID> idsProdutosDomain = servicoDomain.getProdutos().stream()
                            .map(produto -> produto.getProdutoId())
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());
                    servicoEntity.getProdutos().removeIf(produtoJpa -> produtoJpa.getProdutoId() != null
                            && !idsProdutosDomain.contains(produtoJpa.getProdutoId()));

                    for (var produtoDomain : servicoDomain.getProdutos()) {
                        Optional<OrdemDeServicoProdutosJpaEntity> produtoOpt = servicoEntity.getProdutos().stream()
                                .filter(p -> p.getProdutoId() != null
                                        && p.getProdutoId().equals(produtoDomain.getProdutoId()))
                                .findFirst();

                        if (produtoOpt.isEmpty()) {
                            servicoEntity.getProdutos().add(
                                    OrdemDeServicoProdutosJpaEntity.fromDomain(produtoDomain, servicoEntity.getId()));
                        } else {
                            OrdemDeServicoProdutosJpaEntity produtoEntity = produtoOpt.get();
                            produtoEntity.setQuantidade(produtoDomain.getQuantidade());
                            produtoEntity.setValorTotal(produtoDomain.getTotal());
                        }
                    }
                }
            }
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






