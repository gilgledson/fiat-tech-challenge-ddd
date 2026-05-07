package br.com.fiap.oficina.api.modules.operacional.funcionario.application.usecase;

import br.com.fiap.oficina.api.modules.operacional.funcionario.application.dto.FuncionarioOutput;
import br.com.fiap.oficina.api.modules.operacional.funcionario.domain.valueObject.CargoFuncionario;
import java.util.UUID;

public interface CadastrarFuncionarioUseCase {
    FuncionarioOutput executar(UUID usuarioId, String nome, String sobrenome, String cpf, String telefone, CargoFuncionario cargo);
}






