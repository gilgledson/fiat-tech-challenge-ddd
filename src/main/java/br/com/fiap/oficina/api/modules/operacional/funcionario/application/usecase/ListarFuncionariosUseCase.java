package br.com.fiap.oficina.api.modules.operacional.funcionario.application.usecase;

import br.com.fiap.oficina.api.modules.operacional.funcionario.application.dto.FuncionarioOutput;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

public interface ListarFuncionariosUseCase {
    Pagina<FuncionarioOutput> executar(int pagina, int tamanhoLista, boolean incluirInativos);
}






