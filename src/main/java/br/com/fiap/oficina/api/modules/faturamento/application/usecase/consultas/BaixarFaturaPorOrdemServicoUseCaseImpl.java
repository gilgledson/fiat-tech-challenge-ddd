package br.com.fiap.oficina.api.modules.faturamento.application.usecase.consultas;

import br.com.fiap.oficina.api.modules.faturamento.application.repository.FaturaRepository;
import br.com.fiap.oficina.api.modules.faturamento.domain.entity.Fatura;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.UUID;

@ApplicationScoped
public class BaixarFaturaPorOrdemServicoUseCaseImpl implements BaixarFaturaPorOrdemServicoUseCase {

    @Inject
    FaturaRepository faturaRepository;

    @Inject
    BaixarFaturaUseCase baixarFaturaUseCase;

    @Override
    public byte[] executar(UUID ordemServicoId) {
        Fatura fatura = buscarPorOrdemServicoId(ordemServicoId);
        return baixarFaturaUseCase.executar(fatura.getId());
    }

    @Override
    public Fatura buscarPorOrdemServicoId(UUID ordemServicoId) {
        return faturaRepository.buscarPorOrdemServicoId(ordemServicoId)
                .orElseThrow(
                        () -> new NotFoundException("Fatura não encontrada para a ordem de serviço " + ordemServicoId));
    }
}
