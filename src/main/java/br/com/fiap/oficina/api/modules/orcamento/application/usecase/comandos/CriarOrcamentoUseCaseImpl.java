package br.com.fiap.oficina.api.modules.orcamento.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.orcamento.application.repository.OrcamentoRepository;
import br.com.fiap.oficina.api.modules.orcamento.domain.entity.Orcamento;

public class CriarOrcamentoUseCaseImpl implements CriarOrcamentoUseCase {
    public CriarOrcamentoUseCaseImpl(OrcamentoRepository orcamentoRepository) {
        this.orcamentoRepository = orcamentoRepository;
    }

    private final OrcamentoRepository orcamentoRepository;

    @Override
    public void execute(Orcamento orcamento) {
        orcamentoRepository.salvar(orcamento);
    }

}








