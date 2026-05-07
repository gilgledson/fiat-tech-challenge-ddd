package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.ciclovida;

import java.util.UUID;

public interface CancelarOrdemDeServicoUseCase {
    void executar(UUID id, String motivo);
}






