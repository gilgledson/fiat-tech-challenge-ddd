package br.com.fiap.oficina.api.modules.operacional.funcionario.application.repository;

import br.com.fiap.oficina.api.modules.operacional.funcionario.domain.entity.Funcionario;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

import java.util.Optional;
import java.util.UUID;

public interface FuncionarioRepository {
    Optional<Funcionario> buscarPorId(UUID id);

    Optional<Funcionario> buscarPorCpf(String cpf);

    Optional<Funcionario> buscarPorUsuarioId(UUID usuarioId);

    Funcionario salvar(Funcionario funcionario);

    Funcionario atualizar(Funcionario funcionario);

    void deletar(UUID id);

    Pagina<Funcionario> listarTodos(int pagina, int tamanhoLista, boolean incluirInativos);
}






