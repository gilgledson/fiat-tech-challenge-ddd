package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto.AdicionarServicoRequest;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoProdutoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoServicoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.ProdutoSnapshotDTO;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.ServicoSnapshotDTO;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.mapper.OrdemDeServicoOutputMapper;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoProdutos;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicos;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoServicoAdicionado;
import io.vertx.core.eventbus.EventBus;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class AdicionarServicoOrdemDeServicoUseCaseImpl implements AdicionarServicoOrdemDeServicoUseCase {

    private final OrdemDeServicoRepository osRepository;
    private final CatalogoServicoGateway servicoGateway;
    private final CatalogoProdutoGateway produtoGateway;
    private final EventBus eventBus;

    @Override
    @Transactional
    public OrdemDeServicoOutput executar(UUID ordemDeServicoId, AdicionarServicoRequest request) {
        OrdemDeServico ordem = osRepository.buscarPorId(ordemDeServicoId)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        if (ordem.getStatus().isEncerrada()) {
            throw new IllegalArgumentException("Não é possível adicionar serviços a uma ordem de serviço encerrada.");
        }

        ServicoSnapshotDTO servicoCatalogo = servicoGateway.buscarPorId(request.servicoId())
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado no catálogo"));

        OrdemDeServicoServicos item = new OrdemDeServicoServicos();
        item.setId(UUID.randomUUID());
        item.setOrdemDeServicoId(ordemDeServicoId);
        item.setServicoId(request.servicoId());
        item.setNome(servicoCatalogo.nome());
        item.setQuantidade(request.quantidade());
        item.setPrecoUnitario(servicoCatalogo.precoBase());
        item.setStatus(OrdemDeServicoServicoStatus.PENDENTE);
        item.setTipo(servicoCatalogo.tipo());

        // Processar produtos associados
        if (request.produtos() != null) {
            for (var prodReq : request.produtos()) {
                ProdutoSnapshotDTO produtoSnapshot = produtoGateway.buscarPorId(prodReq.produtoId())
                        .orElseThrow(() -> new NotFoundException("Produto " + prodReq.produtoId() + " não encontrado"));

                produtoGateway.reservarEstoque(prodReq.produtoId(), prodReq.quantidade());

                OrdemDeServicoProdutos prodItem = new OrdemDeServicoProdutos();
                prodItem.setOrdemDeServicoId(ordemDeServicoId);
                prodItem.setProdutoId(prodReq.produtoId());
                prodItem.setNomeDoProduto(produtoSnapshot.nome());
                prodItem.setQuantidade(prodReq.quantidade());
                prodItem.setPrecoUnitario(produtoSnapshot.precoUnitario());
                prodItem.setTotal(prodItem.total());
                
                item.getProdutos().add(prodItem);
            }
        }

        item.calcularTotal();

        ordem.getServicos().add(item);
        osRepository.atualizar(ordem);

        eventBus.publish(OrdemServicoServicoAdicionado.TOPICO, 
            new OrdemServicoServicoAdicionado(ordemDeServicoId, ordem.getClienteId(), item.getServicoId(), item.getNome()).toJson());

        return OrdemDeServicoOutputMapper.toOutput(ordem);
    }
}






