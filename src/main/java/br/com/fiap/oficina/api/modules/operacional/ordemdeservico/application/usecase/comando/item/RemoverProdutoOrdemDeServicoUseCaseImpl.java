package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoProdutoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.mapper.OrdemDeServicoOutputMapper;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoProdutos;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class RemoverProdutoOrdemDeServicoUseCaseImpl implements RemoverProdutoOrdemDeServicoUseCase {

    private final OrdemDeServicoRepository osRepository;
    private final CatalogoProdutoGateway produtoGateway;

    @Override
    public OrdemDeServicoOutput executar(UUID ordemDeServicoId, UUID produtoId) {
        OrdemDeServico ordem = osRepository.buscarPorId(ordemDeServicoId)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        if (ordem.getStatus().isEncerrada()) {
            throw new IllegalArgumentException("Não é possível remover produtos de uma ordem de serviço encerrada.");
        }

        OrdemDeServicoProdutos itemARemover = ordem.getProdutos().stream()
                .filter(p -> p.getProdutoId().equals(produtoId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Produto não encontrado nesta ordem de serviço"));

        produtoGateway.liberarEstoqueReservado(produtoId, itemARemover.getQuantidade());

        ordem.getProdutos().remove(itemARemover);
        osRepository.atualizar(ordem);

        return OrdemDeServicoOutputMapper.toOutput(ordem);
    }
}
