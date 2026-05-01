package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.infrastructure.gateway;

import br.com.fiap.oficina.api.modules.faturamento.application.dto.OrdemServicoFaturamentoDTO;
import br.com.fiap.oficina.api.modules.faturamento.application.gateway.OperacionalGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@ApplicationScoped
@Getter
@Setter
public class FaturamentoOperacionalGatewayImpl implements OperacionalGateway {

        @Inject
        OrdemDeServicoRepository ordemDeServicoRepository;

        @Override
        public Optional<OrdemServicoFaturamentoDTO> buscarOrdemDeServicoPorId(UUID id) {
                return ordemDeServicoRepository.buscarPorId(id)
                                .map(os -> new OrdemServicoFaturamentoDTO(
                                                os.getId(),
                                                os.getClienteId(),
                                                os.getVeiculoId(),
                                                os.getServicos().stream()
                                                                .filter(s -> s.getStatus() == br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus.APROVADO ||
                                                                             s.getStatus() == br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus.EM_EXECUCAO ||
                                                                             s.getStatus() == br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus.FINALIZADO)
                                                                .map(s -> new OrdemServicoFaturamentoDTO.ItemServicoDTO(
                                                                                s.getNome(),
                                                                                s.getQuantidade(),
                                                                                s.getPrecoUnitario(),
                                                                                s.getTotal(),
                                                                                s.getProdutos().stream()
                                                                                                .map(p -> new OrdemServicoFaturamentoDTO.ItemProdutoDTO(
                                                                                                                p.getNomeDoProduto(),
                                                                                                                p.getQuantidade(),
                                                                                                                p.getPrecoUnitario(),
                                                                                                                p.getTotal()))
                                                                                                .collect(Collectors.toList())))
                                                                .collect(Collectors.toList()),
                                                os.calcularValorTotal()));
        }
}
