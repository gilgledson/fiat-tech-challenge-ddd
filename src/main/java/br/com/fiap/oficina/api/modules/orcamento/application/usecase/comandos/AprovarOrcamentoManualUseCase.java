package br.com.fiap.oficina.api.modules.orcamento.application.usecase.comandos;

import java.util.List;
import java.util.UUID;

public interface AprovarOrcamentoManualUseCase {
    void executar(UUID orcamentoId, String assinaturaUrl, List<UUID> servicosAceitos, List<UUID> servicosRejeitados);
}






