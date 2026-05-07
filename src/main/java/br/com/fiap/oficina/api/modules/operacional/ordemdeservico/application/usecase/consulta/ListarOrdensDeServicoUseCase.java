package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.consulta;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;
import java.util.UUID;

public interface ListarOrdensDeServicoUseCase {
    Pagina<OrdemDeServicoOutput> executar(UUID clienteId, UUID veiculoId, int pagina, int tamanho, boolean incluirInativas);
}






