package br.com.fiap.oficina.api.modules.atendimento.infrastructure.gateway;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.ClienteSnapshotDTO;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.VeiculoSnapshotDTO;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.AtendimentoGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class AtendimentoGatewayImpl implements AtendimentoGateway {

    @Inject
    ClienteRepository clienteRepository;

    @Inject
    VeiculoRepository veiculoRepository;

    @Override
    public Optional<ClienteSnapshotDTO> buscarClientePorId(UUID id) {
        return clienteRepository.buscarPorId(id)
                .map(c -> new ClienteSnapshotDTO(c.getId(), c.getNome(), c.getCpfCnpj()));
    }

    @Override
    public Optional<VeiculoSnapshotDTO> buscarVeiculoPorId(UUID id) {
        return veiculoRepository.buscarPorId(id)
                .map(v -> new VeiculoSnapshotDTO(v.getId(), v.getClienteId(), v.getPlaca(), v.getModelo(), v.getMarca()));
    }
}






