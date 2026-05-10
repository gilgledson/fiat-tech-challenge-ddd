package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.mapper.OrdemDeServicoOutputMapper;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoRejeitada;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoProdutoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus;
import io.vertx.core.eventbus.EventBus;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class RejeitarOrcamentoUseCaseImpl implements RejeitarOrcamentoUseCase {

    private final OrdemDeServicoRepository repository;
    private final CatalogoProdutoGateway produtoGateway;
    private final EventBus eventBus;

    @Override
    public OrdemDeServicoOutput executar(UUID id) {
        OrdemDeServico ordem = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        if (ordem.getStatus().isEncerrada()) {
            throw new IllegalArgumentException(
                    "Não é possível rejeitar orçamento para uma ordem de serviço encerrada.");
        }

        ordem.setStatus(OrdemDeServicoStatus.REJEITADA);
        
        // Libera estoque de todos os serviços da OS
        ordem.getServicos().forEach(s -> {
            s.setStatus(OrdemDeServicoServicoStatus.CANCELADO);
            if (s.getProdutos() != null) {
                s.getProdutos().forEach(p -> 
                    produtoGateway.liberarEstoqueReservado(p.getProdutoId(), p.getQuantidade())
                );
            }
        });

        repository.atualizar(ordem);

        OrdemServicoRejeitada event = new OrdemServicoRejeitada(ordem.getId());
        eventBus.publish(OrdemServicoRejeitada.TOPICO, event.toJson());

        return OrdemDeServicoOutputMapper.toOutput(ordem);
    }
}






