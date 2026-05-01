package br.com.fiap.oficina.api.modules.faturamento.application.gateway;

import br.com.fiap.oficina.api.modules.faturamento.application.dto.ClienteFaturamentoDTO;
import br.com.fiap.oficina.api.modules.faturamento.application.dto.VeiculoFaturamentoDTO;
import java.util.Optional;
import java.util.UUID;

public interface AtendimentoGateway {
    Optional<ClienteFaturamentoDTO> buscarClientePorId(UUID id);
    Optional<VeiculoFaturamentoDTO> buscarVeiculoPorId(UUID id);
}
