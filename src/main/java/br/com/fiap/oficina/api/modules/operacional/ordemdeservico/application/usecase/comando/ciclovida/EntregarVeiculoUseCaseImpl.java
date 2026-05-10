package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.ciclovida;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoEntregue;
import io.vertx.core.eventbus.EventBus;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class EntregarVeiculoUseCaseImpl implements EntregarVeiculoUseCase {

    private final OrdemDeServicoRepository repository;
    private final EventBus eventBus;

    @Override
    public void executar(UUID id) {
        OrdemDeServico ordem = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        if (ordem.getStatus() != OrdemDeServicoStatus.PAGA) {
            throw new IllegalArgumentException("O veículo só pode ser entregue se a ordem de serviço estiver PAGA.");
        }

        ordem.setStatus(OrdemDeServicoStatus.ENTREGUE);
        repository.atualizar(ordem);

        OrdemServicoEntregue event = new OrdemServicoEntregue(ordem.getId(), ordem.getClienteId());
        eventBus.publish(OrdemServicoEntregue.TOPICO, event.toJson());
    }
}






