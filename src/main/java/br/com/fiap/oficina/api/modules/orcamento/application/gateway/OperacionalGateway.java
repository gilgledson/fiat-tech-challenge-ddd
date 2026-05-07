package br.com.fiap.oficina.api.modules.orcamento.application.gateway;

import br.com.fiap.oficina.api.modules.orcamento.application.dto.OrdemServicoOrcamentoDTO;
import java.util.Optional;
import java.util.UUID;

public interface OperacionalGateway {
    Optional<OrdemServicoOrcamentoDTO> buscarOrdemDeServicoPorId(UUID id);
}






