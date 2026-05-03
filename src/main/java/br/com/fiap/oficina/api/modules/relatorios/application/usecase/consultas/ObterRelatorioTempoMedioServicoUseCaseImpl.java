package br.com.fiap.oficina.api.modules.relatorios.application.usecase.consultas;

import java.util.List;

import br.com.fiap.oficina.api.modules.relatorios.api.dto.RelatorioTempoMedioServicoResponse;
import br.com.fiap.oficina.api.modules.relatorios.application.repository.RelatorioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ObterRelatorioTempoMedioServicoUseCaseImpl implements ObterRelatorioTempoMedioServicoUseCase {

    private final RelatorioRepository repository;

    @Inject
    public ObterRelatorioTempoMedioServicoUseCaseImpl(RelatorioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<RelatorioTempoMedioServicoResponse> executar() {
        return repository.buscarTemposMediosServicos().stream()
                .map(r -> new RelatorioTempoMedioServicoResponse(
                        r.servicoId(),
                        r.nomeServico(),
                        r.quantidadeExecucoesHistoricas(),
                        r.tempoMedioMinutos()
                ))
                .toList();
    }
}
