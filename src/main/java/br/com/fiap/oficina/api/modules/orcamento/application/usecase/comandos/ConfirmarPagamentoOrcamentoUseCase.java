package br.com.fiap.oficina.api.modules.orcamento.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.orcamento.domain.valueObject.MetodoPagamento;
import java.util.UUID;

public interface ConfirmarPagamentoOrcamentoUseCase {
    void execute(UUID orcamentoId, MetodoPagamento metodoPagamento);
}






