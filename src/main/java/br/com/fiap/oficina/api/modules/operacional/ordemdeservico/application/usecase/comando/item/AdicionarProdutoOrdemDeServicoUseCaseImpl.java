package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoProdutoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.ProdutoSnapshotDTO;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.mapper.OrdemDeServicoOutputMapper;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoProdutos;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
public class AdicionarProdutoOrdemDeServicoUseCaseImpl implements AdicionarProdutoOrdemDeServicoUseCase {

    private final OrdemDeServicoRepository osRepository;
    private final CatalogoProdutoGateway produtoGateway;

    @Override
    @Transactional
    public OrdemDeServicoOutput executar(UUID ordemDeServicoId, UUID produtoId, BigDecimal quantidade) {
        OrdemDeServico ordem = osRepository.buscarPorId(ordemDeServicoId)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        if (ordem.getStatus().isEncerrada()) {
            throw new IllegalArgumentException("Não é possível adicionar produtos a uma ordem de serviço encerrada.");
        }

        ProdutoSnapshotDTO produto = produtoGateway.buscarPorId(produtoId)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        produtoGateway.reservarEstoque(produtoId, quantidade);

        OrdemDeServicoProdutos item = new OrdemDeServicoProdutos();
        item.setOrdemDeServicoId(ordemDeServicoId);
        item.setProdutoId(produtoId);
        item.setNomeDoProduto(produto.nome());
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.precoUnitario());
        item.setTotal(item.total());

        ordem.getProdutos().add(item);
        osRepository.atualizar(ordem);

        return OrdemDeServicoOutputMapper.toOutput(ordem);
    }
}
