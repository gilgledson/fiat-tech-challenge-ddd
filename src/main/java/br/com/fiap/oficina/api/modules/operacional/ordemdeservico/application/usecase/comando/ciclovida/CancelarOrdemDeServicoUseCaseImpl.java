package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.ciclovida;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoProdutoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoCancelada;
import io.vertx.core.eventbus.EventBus;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class CancelarOrdemDeServicoUseCaseImpl implements CancelarOrdemDeServicoUseCase {

    private final OrdemDeServicoRepository repository;
    private final CatalogoProdutoGateway produtoGateway;
    private final EventBus eventBus;

    @Override
    public void executar(UUID id, String motivo) {
        OrdemDeServico ordem = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        if (ordem.getStatus().isEncerrada()) {
            throw new IllegalArgumentException("Não é possível cancelar uma ordem de serviço já finalizada.");
        }

        // Libera o estoque reservado de todos os produtos associados aos serviços
        ordem.getServicos().forEach(servico -> {
            servico.getProdutos().forEach(item -> {
                produtoGateway.liberarEstoqueReservado(item.getProdutoId(), item.getQuantidade());
            });
        });

        ordem.setStatus(OrdemDeServicoStatus.CANCELADA);
        ordem.setMotivoCancelamento(motivo);
        ordem.setDeletadoEm(LocalDateTime.now()); // Soft delete de negócio
        repository.atualizar(ordem);

        OrdemServicoCancelada event = new OrdemServicoCancelada(ordem.getId(), motivo);
        eventBus.publish(OrdemServicoCancelada.TOPICO, event.toJson());
    }
}






