package br.com.fiap.oficina.api.modules.orcamento.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.orcamento.application.repository.OrcamentoRepository;
import br.com.fiap.oficina.api.modules.orcamento.domain.entity.Orcamento;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

@ApplicationScoped
public class AtualizarOrcamentoUseCase {

    private final OrcamentoRepository repository;

    public AtualizarOrcamentoUseCase(OrcamentoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void executar(UUID ordemServicoId, BigDecimal novoValor) {
        Orcamento orcamento = repository.buscarPorOrdemServicoId(ordemServicoId)
                .orElseThrow(() -> new RuntimeException("Orcamento não encontrada para OS: " + ordemServicoId));

        orcamento.setValorTotal(novoValor);
        repository.atualizar(orcamento);
    }
}


