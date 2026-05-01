package br.com.fiap.oficina.api.modules.atendimento.infrastructure.gateway;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.modules.faturamento.application.dto.ClienteFaturamentoDTO;
import br.com.fiap.oficina.api.modules.faturamento.application.dto.VeiculoFaturamentoDTO;
import br.com.fiap.oficina.api.modules.faturamento.application.gateway.AtendimentoGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class FaturamentoAtendimentoGatewayImpl implements AtendimentoGateway {

    @Inject
    ClienteRepository clienteRepository;

    @Inject
    VeiculoRepository veiculoRepository;

    @Override
    public Optional<ClienteFaturamentoDTO> buscarClientePorId(UUID id) {
        return clienteRepository.buscarPorId(id)
                .map(c -> {
                    var e = c.getEndereco();
                    String enderecoCompleto = String.format("%s, %s%s - %s, %s - %s, CEP: %s",
                            e.getLogradouro(),
                            e.getNumero(),
                            e.getComplemento() != null && !e.getComplemento().isBlank() ? " (" + e.getComplemento() + ")" : "",
                            e.getBairro(),
                            e.getCidade(),
                            e.getEstado(),
                            e.getCep());
                    return new ClienteFaturamentoDTO(
                            c.getId(),
                            c.getNome(),
                            c.getEmail(),
                            c.getCpfCnpj(),
                            c.getTelefone(),
                            enderecoCompleto
                    );
                });
    }

    @Override
    public Optional<VeiculoFaturamentoDTO> buscarVeiculoPorId(UUID id) {
        return veiculoRepository.buscarPorId(id)
                .map(v -> new VeiculoFaturamentoDTO(
                        v.getId(),
                        v.getPlaca(),
                        v.getMarca(),
                        v.getModelo(),
                        v.getAno()
                ));
    }
}
