package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository;

import java.util.Optional;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

public interface OrdemDeServicoRepository {

    Pagina<OrdemDeServico> buscarTodas(UUID clienteId, UUID veiculoId, int pagina, int tamanho,
            boolean incluirInativas);

    Optional<OrdemDeServico> buscarPorId(UUID id);

    void salvar(OrdemDeServico ordem);

    void atualizar(OrdemDeServico ordem);

    void deletar(UUID id);

}






