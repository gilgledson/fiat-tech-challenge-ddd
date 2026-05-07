package br.com.fiap.oficina.api.modules.orcamento.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.orcamento.domain.entity.Orcamento;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface CriarOrcamentoUseCase {

    public void execute(Orcamento orcamento);

}







