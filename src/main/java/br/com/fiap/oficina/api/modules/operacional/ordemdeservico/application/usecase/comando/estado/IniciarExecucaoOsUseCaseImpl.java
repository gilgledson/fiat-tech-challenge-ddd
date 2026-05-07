package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoExecucaoIniciada;
import io.vertx.core.eventbus.EventBus;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class IniciarExecucaoOsUseCaseImpl implements IniciarExecucaoOsUseCase {

    private final OrdemDeServicoRepository repository;
    private final EventBus eventBus;

    @Override
    public void executar(UUID id) {
        OrdemDeServico ordem = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        if (ordem.getStatus() != OrdemDeServicoStatus.APROVADA ) {
            throw new IllegalArgumentException("Apenas ordens APROVADAS ou EM_EXECUCAO podem iniciar execução.");
        }

        ordem.setStatus(OrdemDeServicoStatus.EM_EXECUCAO);
        ordem.setDataInicioExecucao(LocalDateTime.now());

        repository.atualizar(ordem);

        OrdemServicoExecucaoIniciada event = new OrdemServicoExecucaoIniciada(ordem.getId());
        eventBus.publish(OrdemServicoExecucaoIniciada.TOPICO, event.toJson());
    }
}






