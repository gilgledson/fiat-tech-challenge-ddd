package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.mapper.OrdemDeServicoOutputMapper;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoDiagnosticoConcluido;
import io.vertx.core.eventbus.EventBus;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ConcluirDiagnosticoUseCaseImpl implements ConcluirDiagnosticoUseCase {

    private final OrdemDeServicoRepository repository;
    private final EventBus eventBus;

    @Override
    public OrdemDeServicoOutput executar(UUID id) {
        OrdemDeServico ordem = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        if (ordem.getStatus().isEncerrada()) {
            throw new IllegalArgumentException(
                    "Não é possível concluir diagnóstico para uma ordem de serviço encerrada.");
        }

        ordem.setStatus(OrdemDeServicoStatus.AGUARDANDO_APROVACAO);

        repository.atualizar(ordem);

        OrdemServicoDiagnosticoConcluido event = new OrdemServicoDiagnosticoConcluido(
                ordem.getId(),
                ordem.calcularValorTotal(true),
                ordem.getClienteId());
        eventBus.publish(OrdemServicoDiagnosticoConcluido.TOPICO, event.toJson());

        return OrdemDeServicoOutputMapper.toOutput(ordem);
    }
}






