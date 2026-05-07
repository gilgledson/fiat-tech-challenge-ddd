package br.com.fiap.oficina.api.modules.atendimento.cliente.application.usecase;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.dto.ClienteOutput;
import br.com.fiap.oficina.api.modules.atendimento.cliente.application.mapper.ClienteOutputMapper;
import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.entity.Cliente;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.valueobject.Endereco;

import java.util.UUID;

public class CadastrarClienteUseCaseImpl implements CadastrarClienteUseCase {

    private final ClienteRepository repository;

    public CadastrarClienteUseCaseImpl(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public ClienteOutput executar(UUID usuarioId, String nome, String email, String cpfCnpj, String telefone, Endereco endereco) {
        // Valida se já existe cliente com o mesmo CPF/CNPJ
        repository.buscarPorCpfCnpj(cpfCnpj).ifPresent(c -> {
            throw new IllegalArgumentException("Já existe um cliente cadastrado com este CPF/CNPJ");
        });

        // Valida se já existe cliente com o mesmo usuarioId
        if (usuarioId != null) {
            repository.buscarPorUsuarioId(usuarioId).ifPresent(c -> {
                throw new IllegalArgumentException("Já existe um cliente vinculado a este usuário");
            });
        }

        Cliente cliente = new Cliente(usuarioId, nome, email, cpfCnpj, telefone, endereco);
        repository.salvar(cliente);
        return ClienteOutputMapper.toOutput(cliente);
    }
}






