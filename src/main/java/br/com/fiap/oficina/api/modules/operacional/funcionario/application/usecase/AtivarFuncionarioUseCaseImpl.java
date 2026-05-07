package br.com.fiap.oficina.api.modules.operacional.funcionario.application.usecase;

import br.com.fiap.oficina.api.modules.operacional.funcionario.application.repository.FuncionarioRepository;
import br.com.fiap.oficina.api.modules.operacional.funcionario.domain.entity.Funcionario;
import jakarta.ws.rs.NotFoundException;
import java.util.UUID;

public class AtivarFuncionarioUseCaseImpl implements AtivarFuncionarioUseCase {

    private final FuncionarioRepository repository;

    public AtivarFuncionarioUseCaseImpl(FuncionarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public void executar(UUID id) {
        Funcionario funcionario = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        funcionario.ativar();
        repository.atualizar(funcionario);
    }
}






