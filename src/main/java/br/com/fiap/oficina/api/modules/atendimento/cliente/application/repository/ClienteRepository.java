package br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.entity.Cliente;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

public interface ClienteRepository {

    public void salvar(Cliente cliente);

    public void atualizar(Cliente cliente);

    public void deletar(UUID id);

    public Optional<Cliente> buscarPorId(UUID id);

    public Optional<Cliente> buscarPorUsuarioId(UUID usuarioId);

    public Optional<Cliente> buscarPorCpfCnpj(String cpfCnpj);

    public List<Cliente> buscarPorIds(List<UUID> ids);

    public Pagina<Cliente> listarTodos(int pagina, int tamanhoLista, boolean incluirInativos);

}






