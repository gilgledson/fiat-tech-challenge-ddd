package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoServicoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.ServicoSnapshotDTO;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.mapper.OrdemDeServicoOutputMapper;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicos;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class AdicionarServicoOrdemDeServicoUseCaseImpl implements AdicionarServicoOrdemDeServicoUseCase {

    private final OrdemDeServicoRepository osRepository;
    private final CatalogoServicoGateway servicoGateway;

    @Override
    @Transactional
    public OrdemDeServicoOutput executar(UUID ordemDeServicoId, UUID servicoId, int quantidade) {
        OrdemDeServico ordem = osRepository.buscarPorId(ordemDeServicoId)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        if (ordem.getStatus().isEncerrada()) {
            throw new IllegalArgumentException("Não é possível adicionar serviços a uma ordem de serviço encerrada.");
        }

        ServicoSnapshotDTO servicoCatalogo = servicoGateway.buscarPorId(servicoId)
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado no catálogo"));

        OrdemDeServicoServicos item = new OrdemDeServicoServicos();
        item.setServicoId(servicoId);
        item.setNome(servicoCatalogo.nome());
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(servicoCatalogo.precoBase());
        item.setStatus(OrdemDeServicoServicoStatus.ABERTA);
        item.calcularTotal();

        ordem.getServicos().add(item);
        osRepository.atualizar(ordem);

        return OrdemDeServicoOutputMapper.toOutput(ordem);
    }
}
