package br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase;

import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.dto.ServicoOutput;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

public interface ListarServicosUseCase {
    public Pagina<ServicoOutput> executar(int pagina, int tamanho, boolean incluirInativos);
}






