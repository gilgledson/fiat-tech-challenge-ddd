package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoAprovada;
import io.vertx.core.eventbus.EventBus;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class AprovarOrdemDeServicoUseCaseImpl implements AprovarOrdemDeServicoUseCase {

    private final OrdemDeServicoRepository repository;
    private final EventBus eventBus;

    @Override
    public void executar(UUID id) {
        OrdemDeServico ordem = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        if (ordem.getStatus() != OrdemDeServicoStatus.AGUARDANDO_APROVACAO) {
            throw new IllegalArgumentException("Apenas ordens AGUARDANDO_APROVACAO podem ser aprovadas.");
        }

        ordem.setStatus(OrdemDeServicoStatus.APROVADA);
        repository.atualizar(ordem);

        OrdemServicoAprovada event = new OrdemServicoAprovada(ordem.getId());
        eventBus.publish(OrdemServicoAprovada.TOPICO, event.toJson());
    }
}
