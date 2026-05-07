package br.com.fiap.oficina.api.modules.atendimento.veiculo.domain.entity;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Veiculo {
    private UUID id;
    private UUID clienteId;
    private String placa;
    private String marca;
    private String modelo;
    private int ano;
    private Optional<LocalDateTime> deletadoEm;

    public UUID getId() { return id; }
    public UUID getClienteId() { return clienteId; }
    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAno() { return ano; }
    public Optional<LocalDateTime> getDeletadoEm() { return deletadoEm; }

    public Veiculo(UUID clienteId, String placa, String marca, String modelo, int ano) {
        this.id = UUID.randomUUID();
        this.clienteId = clienteId;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.deletadoEm = Optional.empty();
        validar();
    }

    public static Veiculo reconstituir(UUID id, UUID clienteId, String placa, String marca, String modelo, int ano, Optional<LocalDateTime> deletadoEm) {
        Veiculo veiculo = new Veiculo(id, clienteId, placa, marca, modelo, ano, deletadoEm);
        veiculo.validar();
        return veiculo;
    }

    public void atualizarDados(UUID clienteId, String placa, String marca, String modelo, int ano) {
        this.clienteId = clienteId;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        validar();
    }

    public void inativar() {
        if (this.deletadoEm.isPresent()) {
            throw new IllegalArgumentException("Este veículo já está inativo.");
        }
        this.deletadoEm = Optional.of(LocalDateTime.now());
    }

    public void ativar() {
        if (this.deletadoEm.isEmpty()) {
            throw new IllegalArgumentException("Este veículo já está ativo.");
        }
        this.deletadoEm = Optional.empty();
    }

    private void validar() {
        if (this.clienteId == null) {
            throw new IllegalArgumentException("O cliente é obrigatório");
        }
        if (this.placa == null || this.placa.isBlank()) {
            throw new IllegalArgumentException("A placa é obrigatória");
        }
        if (this.marca == null || this.marca.isBlank()) {
            throw new IllegalArgumentException("A marca é obrigatória");
        }
        if (this.modelo == null || this.modelo.isBlank()) {
            throw new IllegalArgumentException("O modelo é obrigatório");
        }
        if (this.ano < 1900 || this.ano > LocalDateTime.now().getYear() + 1) {
            throw new IllegalArgumentException("Ano inválido");
        }
    }
}






