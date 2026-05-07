package br.com.fiap.oficina.api.modules.catalogo.servico.infrastructure.gateway;

import br.com.fiap.oficina.api.modules.catalogo.servico.application.repository.ServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.ServicoSnapshotDTO;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoServicoGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CatalogoServicoGatewayImpl implements CatalogoServicoGateway {

    @Inject
    ServicoRepository servicoRepository;

    @Override
    public Optional<ServicoSnapshotDTO> buscarPorId(UUID id) {
        return servicoRepository.buscarPorId(id)
                .map(s -> new ServicoSnapshotDTO(s.getId(), s.getNome(), s.getPrecoBase(), s.getTipo()));
    }
}






