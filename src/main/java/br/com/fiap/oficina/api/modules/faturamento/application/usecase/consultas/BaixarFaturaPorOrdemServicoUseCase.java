package br.com.fiap.oficina.api.modules.faturamento.application.usecase.consultas;

import java.util.UUID;
import br.com.fiap.oficina.api.modules.faturamento.domain.entity.Fatura;

public interface BaixarFaturaPorOrdemServicoUseCase {
    byte[] executar(UUID ordemServicoId);
    Fatura buscarPorOrdemServicoId(UUID ordemServicoId);
}
