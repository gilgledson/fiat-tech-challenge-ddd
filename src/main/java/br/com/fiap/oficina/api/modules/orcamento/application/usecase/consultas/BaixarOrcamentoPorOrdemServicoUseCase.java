package br.com.fiap.oficina.api.modules.orcamento.application.usecase.consultas;

import java.util.UUID;
import br.com.fiap.oficina.api.modules.orcamento.domain.entity.Orcamento;

public interface BaixarOrcamentoPorOrdemServicoUseCase {
    byte[] executar(UUID ordemServicoId);
    Orcamento buscarPorOrdemServicoId(UUID ordemServicoId);
}






