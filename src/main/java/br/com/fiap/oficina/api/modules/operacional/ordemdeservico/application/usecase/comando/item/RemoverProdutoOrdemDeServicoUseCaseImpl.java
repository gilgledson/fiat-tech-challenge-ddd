package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoProdutoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.mapper.OrdemDeServicoOutputMapper;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoProdutos;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicos;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
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

        // Procurar o produto dentro de todos os serviços da OS
        OrdemDeServicoServicos servicoPai = null;
        OrdemDeServicoProdutos itemARemover = null;

        for (OrdemDeServicoServicos servico : ordem.getServicos()) {
            Optional<OrdemDeServicoProdutos> produtoOpt = servico.getProdutos().stream()
                    .filter(p -> p.getProdutoId().equals(produtoId))
                    .findFirst();
            
            if (produtoOpt.isPresent()) {
                servicoPai = servico;
                itemARemover = produtoOpt.get();
                break;
            }
        }

        if (itemARemover == null) {
            throw new NotFoundException("Produto não encontrado nesta ordem de serviço");
        }

        produtoGateway.liberarEstoqueReservado(produtoId, itemARemover.getQuantidade());

        servicoPai.getProdutos().remove(itemARemover);
        servicoPai.calcularTotal(); // Recalcula o total do serviço após remover o produto
        
        osRepository.atualizar(ordem);

        return OrdemDeServicoOutputMapper.toOutput(ordem);
    }
}






