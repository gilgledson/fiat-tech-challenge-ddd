package br.com.fiap.oficina.api.modules.faturamento.application.gateway;

import br.com.fiap.oficina.api.modules.faturamento.application.dto.OrdemServicoFaturamentoDTO;
import java.util.Optional;
import java.util.UUID;

public interface OperacionalGateway {
    Optional<OrdemServicoFaturamentoDTO> buscarOrdemDeServicoPorId(UUID id);
}
