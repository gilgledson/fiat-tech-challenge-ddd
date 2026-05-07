package br.com.fiap.oficina.api.modules.atendimento.cliente.application.usecase;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.dto.ClienteOutput;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

public interface ListarClientesUseCase {
    Pagina<ClienteOutput> executar(int pagina, int tamanhoLista, boolean incluirInativos);
}






