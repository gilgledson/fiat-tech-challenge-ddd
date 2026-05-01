package br.com.fiap.oficina.api.modules.faturamento.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.faturamento.application.repository.FaturaRepository;
import br.com.fiap.oficina.api.modules.faturamento.domain.entity.Fatura;

public class CriarFaturaUseCaseImpl implements CriarFaturaUseCase {
    public CriarFaturaUseCaseImpl(FaturaRepository faturaRepository) {
        this.faturaRepository = faturaRepository;
    }

    private final FaturaRepository faturaRepository;

    @Override
    public void execute(Fatura fatura) {
        faturaRepository.salvar(fatura);
    }

}
