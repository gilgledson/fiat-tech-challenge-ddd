package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto.AprovarServicoRequest;
import java.util.UUID;

public interface AprovarOrdemDeServicoUseCase {
    void executar(UUID id, AprovarServicoRequest request);
}






