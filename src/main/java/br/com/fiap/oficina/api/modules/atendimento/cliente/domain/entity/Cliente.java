package br.com.fiap.oficina.api.modules.atendimento.cliente.domain.entity;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.valueobject.Endereco;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Cliente {
    private UUID id;
    private Optional<UUID> usuarioId;
    private String nome;
    private String email;
    private String cpfCnpj;
    private String telefone;
    private Endereco endereco;
    private Optional<LocalDateTime> deletadoEm;

    public Cliente(UUID usuarioId, String nome, String email, String cpfCnpj, String telefone, Endereco endereco) {
        this.id = UUID.randomUUID();
        this.usuarioId = Optional.ofNullable(usuarioId);
        this.nome = nome;
        this.email = email;
        this.cpfCnpj = cpfCnpj;
        this.telefone = telefone;
        this.endereco = endereco;
        this.deletadoEm = Optional.empty();
        validar();
    }

    public static Cliente reconstituir(UUID id, UUID usuarioId, String nome, String email, String cpfCnpj,
            String telefone, Endereco endereco, Optional<LocalDateTime> deletadoEm) {
        Cliente cliente = new Cliente(id, Optional.ofNullable(usuarioId), nome, email, cpfCnpj, telefone, endereco, deletadoEm);
        cliente.validar();
        return cliente;
    }

    public void atualizarDados(String nome, String email, String telefone, Endereco endereco) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        validar();
    }

    public void inativar() {
        if (this.deletadoEm.isPresent()) {
            throw new IllegalArgumentException("Este cliente já está inativo.");
        }
        this.deletadoEm = Optional.of(LocalDateTime.now());
    }

    public void ativar() {
        if (this.deletadoEm.isEmpty()) {
            throw new IllegalArgumentException("Este cliente já está ativo.");
        }
        this.deletadoEm = Optional.empty();
    }

    private void validar() {
        if (this.nome == null || this.nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (this.email == null || this.email.isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (this.cpfCnpj == null || this.cpfCnpj.isBlank()) {
            throw new IllegalArgumentException("CPF/CNPJ é obrigatório");
        }
        if (this.telefone == null || this.telefone.isBlank()) {
            throw new IllegalArgumentException("Telefone é obrigatório");
        }
        if (this.endereco == null) {
            throw new IllegalArgumentException("Endereço é obrigatório");
        }
    }
}






