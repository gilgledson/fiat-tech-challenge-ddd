package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import java.util.UUID;

public interface ConcluirDiagnosticoUseCase {
    OrdemDeServicoOutput executar(UUID id);
}






