package br.com.fiap.oficina.api.modules.atendimento.infrastructure.gateway;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.modules.orcamento.application.dto.ClienteOrcamentoDTO;
import br.com.fiap.oficina.api.modules.orcamento.application.dto.VeiculoOrcamentoDTO;
import br.com.fiap.oficina.api.modules.orcamento.application.gateway.AtendimentoGateway;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class OrcamentoAtendimentoGatewayImpl implements AtendimentoGateway {

        private final ClienteRepository clienteRepository;

        private final VeiculoRepository veiculoRepository;

        @Override
        public Optional<ClienteOrcamentoDTO> buscarClientePorId(UUID id) {
                return clienteRepository.buscarPorId(id)
                                .map(c -> {
                                        var e = c.getEndereco();
                                        String enderecoCompleto = String.format("%s, %s%s - %s, %s - %s, CEP: %s",
                                                        e.getLogradouro(),
                                                        e.getNumero(),
                                                        e.getComplemento() != null && !e.getComplemento().isBlank()
                                                                        ? " (" + e.getComplemento() + ")"
                                                                        : "",
                                                        e.getBairro(),
                                                        e.getCidade(),
                                                        e.getEstado(),
                                                        e.getCep());
                                        return new ClienteOrcamentoDTO(
                                                        c.getId(),
                                                        c.getNome(),
                                                        c.getEmail(),
                                                        c.getCpfCnpj(),
                                                        c.getTelefone(),
                                                        enderecoCompleto);
                                });
        }

        @Override
        public Optional<VeiculoOrcamentoDTO> buscarVeiculoPorId(UUID id) {
                return veiculoRepository.buscarPorId(id)
                                .map(v -> new VeiculoOrcamentoDTO(
                                                v.getId(),
                                                v.getPlaca(),
                                                v.getMarca(),
                                                v.getModelo(),
                                                v.getAno()));
        }
}
