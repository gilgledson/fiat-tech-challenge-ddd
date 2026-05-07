package br.com.fiap.oficina.api.modules.atendimento.veiculo.application.usecase;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.dto.VeiculoOutput;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

public interface ListarVeiculosUseCase {
    Pagina<VeiculoOutput> executar(int pagina, int tamanhoLista, boolean incluirInativos);
}






