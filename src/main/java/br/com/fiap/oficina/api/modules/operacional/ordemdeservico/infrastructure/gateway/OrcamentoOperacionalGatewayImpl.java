package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.infrastructure.gateway;

import br.com.fiap.oficina.api.modules.orcamento.application.dto.OrdemServicoOrcamentoDTO;
import br.com.fiap.oficina.api.modules.orcamento.application.gateway.OperacionalGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@ApplicationScoped
@Getter
@Setter
@RequiredArgsConstructor
public class OrcamentoOperacionalGatewayImpl implements OperacionalGateway {

        private final OrdemDeServicoRepository ordemDeServicoRepository;

        @Override
        public Optional<OrdemServicoOrcamentoDTO> buscarOrdemDeServicoPorId(UUID id) {
                return ordemDeServicoRepository.buscarPorId(id)
                                .map(os -> new OrdemServicoOrcamentoDTO(
                                                os.getId(),
                                                os.getClienteId(),
                                                os.getVeiculoId(),
                                                os.getServicos().stream()
                                                                .filter(s -> s.getStatus() == br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus.APROVADO
                                                                                ||
                                                                                s.getStatus() == br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus.EM_EXECUCAO
                                                                                ||
                                                                                s.getStatus() == br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus.FINALIZADO)
                                                                .map(s -> new OrdemServicoOrcamentoDTO.ItemServicoDTO(
                                                                                s.getNome(),
                                                                                s.getQuantidade(),
                                                                                s.getPrecoUnitario(),
                                                                                s.getTotal(),
                                                                                s.getProdutos().stream()
                                                                                                .map(p -> new OrdemServicoOrcamentoDTO.ItemProdutoDTO(
                                                                                                                p.getNomeDoProduto(),
                                                                                                                p.getQuantidade(),
                                                                                                                p.getPrecoUnitario(),
                                                                                                                p.getTotal()))
                                                                                                .collect(Collectors
                                                                                                                .toList())))
                                                                .collect(Collectors.toList()),
                                                os.calcularValorTotal()));
        }
}
