package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item;

import java.util.UUID;

public interface FinalizarExecucaoServicoUseCase {
    void executar(UUID ordemId, UUID id);
}






