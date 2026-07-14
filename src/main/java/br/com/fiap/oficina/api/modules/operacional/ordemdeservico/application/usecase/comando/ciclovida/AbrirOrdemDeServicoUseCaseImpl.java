package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.ciclovida;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto.AdicionarServicoRequest;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.AtendimentoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.VeiculoSnapshotDTO;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.mapper.OrdemDeServicoOutputMapper;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item.AdicionarServicoOrdemDeServicoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoAberta;
import io.vertx.core.eventbus.EventBus;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AbrirOrdemDeServicoUseCaseImpl implements AbrirOrdemDeServicoUseCase {

    private final OrdemDeServicoRepository repository;
    private final AtendimentoGateway atendimentoGateway;
    private final EventBus eventBus;
    private final AdicionarServicoOrdemDeServicoUseCase adicionarServicoUseCase;

    @Override
    public OrdemDeServicoOutput executar(UUID clienteId, UUID veiculoId, String descricaoProblema,
            List<AdicionarServicoRequest> servicos) {

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
        ordem.setServicos(new ArrayList<>());

        repository.salvar(ordem);

        OrdemServicoAberta event = new OrdemServicoAberta(ordem.getId(), ordem.getClienteId(), ordem.getVeiculoId());
        eventBus.publish(OrdemServicoAberta.TOPICO, event.toJson());

        // Quando o cliente já chega com uma demanda específica, os serviços (e peças
        // associadas) podem ser informados direto na abertura, em vez de esperar o
        // diagnóstico do mecânico.
        OrdemDeServicoOutput output = OrdemDeServicoOutputMapper.toOutput(ordem);
        if (servicos != null) {
            for (AdicionarServicoRequest servicoRequest : servicos) {
                output = adicionarServicoUseCase.executar(ordem.getId(), servicoRequest);
            }
        }

        return output;
    }
}






