package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.ciclovida;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.AtendimentoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.VeiculoSnapshotDTO;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.mapper.OrdemDeServicoOutputMapper;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoAberta;
import io.vertx.core.eventbus.EventBus;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@RequiredArgsConstructor
public class AbrirOrdemDeServicoUseCaseImpl implements AbrirOrdemDeServicoUseCase {

    private final OrdemDeServicoRepository repository;
    private final AtendimentoGateway atendimentoGateway;
    private final EventBus eventBus;

    @Override
    public OrdemDeServicoOutput executar(UUID clienteId, UUID veiculoId, String descricaoProblema) {

        atendimentoGateway.buscarClientePorId(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado para abertura de OS"));

        VeiculoSnapshotDTO veiculo = atendimentoGateway.buscarVeiculoPorId(veiculoId)
                .orElseThrow(() -> new NotFoundException("Veículo não encontrado para abertura de OS"));

        if (!veiculo.clienteId().equals(clienteId)) {
            throw new IllegalArgumentException("O veículo informado não pertence ao cliente informado.");
        }

        OrdemDeServico ordem = new OrdemDeServico();
        ordem.setId(UUID.randomUUID());
        ordem.setClienteId(clienteId);
        ordem.setVeiculoId(veiculoId);
        ordem.setDescricaoProblema(descricaoProblema);
        ordem.setStatus(OrdemDeServicoStatus.ABERTA);
        ordem.setDataAbertura(LocalDateTime.now());
        ordem.setProdutos(new ArrayList<>());
        ordem.setServicos(new ArrayList<>());

        repository.salvar(ordem);

        OrdemServicoAberta event = new OrdemServicoAberta(ordem.getId(), ordem.getClienteId(), ordem.getVeiculoId());
        eventBus.publish(OrdemServicoAberta.TOPICO, event.toJson());

        return OrdemDeServicoOutputMapper.toOutput(ordem);
    }
}
