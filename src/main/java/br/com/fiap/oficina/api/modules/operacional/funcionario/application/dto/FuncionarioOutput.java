package br.com.fiap.oficina.api.modules.operacional.funcionario.application.dto;

import br.com.fiap.oficina.api.modules.operacional.funcionario.domain.valueObject.CargoFuncionario;
import java.time.LocalDateTime;
import java.util.UUID;

public record FuncionarioOutput(
    UUID id,
    UUID usuarioId,
    String nome,
    String sobrenome,
    String cpf,
    String telefone,
    CargoFuncionario cargo,
    boolean ativo,
    LocalDateTime deletadoEm
) {}






