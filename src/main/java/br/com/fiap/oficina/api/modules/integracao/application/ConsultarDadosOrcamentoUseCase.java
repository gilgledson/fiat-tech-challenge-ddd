package br.com.fiap.oficina.api.modules.integracao.application;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

/**
 * Dados da OS, do cliente e do veículo no formato que o Billing Service usa
 * para montar o orçamento. No monólito essa leitura era feita pelos gateways
 * do módulo de orçamento, direto nos repositórios.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class ConsultarDadosOrcamentoUseCase {

    /** Só entram no orçamento os serviços que o cliente aprovou (em qualquer estágio de execução). */
    private static final Set<OrdemDeServicoServicoStatus> STATUS_ORCADOS = Set.of(
            OrdemDeServicoServicoStatus.APROVADO,
            OrdemDeServicoServicoStatus.EM_EXECUCAO,
            OrdemDeServicoServicoStatus.FINALIZADO);

    private final OrdemDeServicoRepository ordemDeServicoRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;

    public Optional<DadosOrcamento.OrdemServico> buscarOrdemDeServico(UUID id) {
        return ordemDeServicoRepository.buscarPorId(id)
                .map(os -> new DadosOrcamento.OrdemServico(
                        os.getId(),
                        os.getClienteId(),
                        os.getVeiculoId(),
                        os.getServicos().stream()
                                .filter(s -> STATUS_ORCADOS.contains(s.getStatus()))
                                .map(s -> new DadosOrcamento.ItemServico(
                                        s.getNome(),
                                        s.getQuantidade(),
                                        s.getPrecoUnitario(),
                                        s.getTotal(),
                                        s.getProdutos().stream()
                                                .map(p -> new DadosOrcamento.ItemProduto(
                                                        p.getNomeDoProduto(),
                                                        p.getQuantidade(),
                                                        p.getPrecoUnitario(),
                                                        p.getTotal()))
                                                .toList()))
                                .toList(),
                        os.calcularValorTotal()));
    }

    public Optional<DadosOrcamento.Cliente> buscarCliente(UUID id) {
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
                    return new DadosOrcamento.Cliente(c.getId(), c.getNome(), c.getEmail(), c.getCpfCnpj(),
                            c.getTelefone(), enderecoCompleto);
                });
    }

    public Optional<DadosOrcamento.Veiculo> buscarVeiculo(UUID id) {
        return veiculoRepository.buscarPorId(id)
                .map(v -> new DadosOrcamento.Veiculo(v.getId(), v.getPlaca(), v.getMarca(), v.getModelo(),
                        v.getAno()));
    }
}
