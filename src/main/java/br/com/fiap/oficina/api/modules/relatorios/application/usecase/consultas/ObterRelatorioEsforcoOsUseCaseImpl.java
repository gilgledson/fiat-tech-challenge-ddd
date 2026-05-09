package br.com.fiap.oficina.api.modules.relatorios.application.usecase.consultas;

import java.util.Optional;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.relatorios.api.dto.RelatorioEsforcoOsResponse;
import br.com.fiap.oficina.api.modules.relatorios.application.repository.RelatorioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class ObterRelatorioEsforcoOsUseCaseImpl implements ObterRelatorioEsforcoOsUseCase {

    private final RelatorioRepository repository;

    @Override
    public Optional<RelatorioEsforcoOsResponse> executar(UUID osId) {
        return repository.buscarEsforcoPorOsId(osId)
                .map(r -> new RelatorioEsforcoOsResponse(
                        r.ordemDeServicoId(),
                        r.totalServicosRealizados(),
                        r.esforcoTotalMinutos()));
    }
}
