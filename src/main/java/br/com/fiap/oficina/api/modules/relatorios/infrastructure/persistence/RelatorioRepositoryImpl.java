package br.com.fiap.oficina.api.modules.relatorios.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.relatorios.application.repository.RelatorioRepository;
import br.com.fiap.oficina.api.modules.relatorios.domain.RelatorioEsforcoOs;
import br.com.fiap.oficina.api.modules.relatorios.domain.RelatorioTempoMedioServico;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class RelatorioRepositoryImpl implements RelatorioRepository {

    private final EntityManager em;

    @Override
    public Optional<RelatorioEsforcoOs> buscarEsforcoPorOsId(UUID osId) {
        RelatorioEsforcoOsJpaEntity entity = em.find(RelatorioEsforcoOsJpaEntity.class, osId);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(new RelatorioEsforcoOs(
                entity.getOrdemDeServicoId(),
                entity.getTotalServicosRealizados(),
                entity.getEsforcoTotalMinutos()));
    }

    @Override
    public List<RelatorioTempoMedioServico> buscarTemposMediosServicos() {
        List<RelatorioTempoMedioServicoJpaEntity> entities = em.createQuery(
                "SELECT r FROM RelatorioTempoMedioServicoJpaEntity r",
                RelatorioTempoMedioServicoJpaEntity.class).getResultList();

        return entities.stream()
                .map(e -> new RelatorioTempoMedioServico(
                        e.getServicoId(),
                        e.getNomeServico(),
                        e.getQuantidadeExecucoesHistoricas(),
                        e.getTempoMedioMinutos()))
                .toList();
    }
}
