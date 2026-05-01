package br.com.fiap.oficina.api.modules.faturamento.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.faturamento.domain.valueObject.MetodoPagamento;
import java.util.UUID;

public interface ConfirmarPagamentoFaturaUseCase {
    void execute(UUID faturaId, MetodoPagamento metodoPagamento);
}
