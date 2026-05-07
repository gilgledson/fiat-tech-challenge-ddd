package br.com.fiap.oficina.api.modules.orcamento.application.usecase.consultas;

import br.com.fiap.oficina.api.modules.orcamento.application.repository.OrcamentoRepository;
import br.com.fiap.oficina.api.modules.orcamento.domain.entity.Orcamento;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.UUID;

@ApplicationScoped
public class BaixarOrcamentoPorOrdemServicoUseCaseImpl implements BaixarOrcamentoPorOrdemServicoUseCase {

    @Inject
    OrcamentoRepository orcamentoRepository;

    @Inject
    BaixarOrcamentoUseCase baixarOrcamentoUseCase;

    @Override
    public byte[] executar(UUID ordemServicoId) {
        Orcamento orcamento = buscarPorOrdemServicoId(ordemServicoId);
        return baixarOrcamentoUseCase.executar(orcamento.getId());
    }

    @Override
    public Orcamento buscarPorOrdemServicoId(UUID ordemServicoId) {
        return orcamentoRepository.buscarPorOrdemServicoId(ordemServicoId)
                .orElseThrow(
                        () -> new NotFoundException("Orcamento não encontrada para a ordem de serviço " + ordemServicoId));
    }
}








