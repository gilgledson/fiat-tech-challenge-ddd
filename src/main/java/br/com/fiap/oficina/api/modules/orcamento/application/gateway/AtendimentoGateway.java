package br.com.fiap.oficina.api.modules.orcamento.application.gateway;

import br.com.fiap.oficina.api.modules.orcamento.application.dto.ClienteOrcamentoDTO;
import br.com.fiap.oficina.api.modules.orcamento.application.dto.VeiculoOrcamentoDTO;
import java.util.Optional;
import java.util.UUID;

public interface AtendimentoGateway {
    Optional<ClienteOrcamentoDTO> buscarClientePorId(UUID id);
    Optional<VeiculoOrcamentoDTO> buscarVeiculoPorId(UUID id);
}






