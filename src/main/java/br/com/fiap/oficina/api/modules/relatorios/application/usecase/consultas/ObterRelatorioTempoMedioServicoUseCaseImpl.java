package br.com.fiap.oficina.api.modules.relatorios.application.usecase.consultas;

import java.util.List;

import br.com.fiap.oficina.api.modules.relatorios.api.dto.RelatorioTempoMedioServicoResponse;
import br.com.fiap.oficina.api.modules.relatorios.application.repository.RelatorioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class ObterRelatorioTempoMedioServicoUseCaseImpl implements ObterRelatorioTempoMedioServicoUseCase {

    private final RelatorioRepository repository;

    @Override
    public List<RelatorioTempoMedioServicoResponse> executar() {
        return repository.buscarTemposMediosServicos().stream()
                .map(r -> new RelatorioTempoMedioServicoResponse(
                        r.servicoId(),
                        r.nomeServico(),
                        r.quantidadeExecucoesHistoricas(),
                        r.tempoMedioMinutos()))
                .toList();
    }
}
