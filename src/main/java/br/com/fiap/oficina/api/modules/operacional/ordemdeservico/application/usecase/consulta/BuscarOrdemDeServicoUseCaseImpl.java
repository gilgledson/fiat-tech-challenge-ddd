package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.consulta;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.mapper.OrdemDeServicoOutputMapper;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class BuscarOrdemDeServicoUseCaseImpl implements BuscarOrdemDeServicoUseCase {

    private final OrdemDeServicoRepository repository;

    @Override
    public OrdemDeServicoOutput executar(UUID id) {
        OrdemDeServico ordem = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço com id " + id + " não encontrada"));

        return OrdemDeServicoOutputMapper.toOutput(ordem);
    }
}






