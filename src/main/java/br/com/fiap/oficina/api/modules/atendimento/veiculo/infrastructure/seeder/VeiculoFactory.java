package br.com.fiap.oficina.api.modules.atendimento.veiculo.infrastructure.seeder;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.domain.entity.Veiculo;
import jakarta.enterprise.context.ApplicationScoped;
import net.datafaker.Faker;

import java.util.Locale;
import java.util.UUID;

@ApplicationScoped
public class VeiculoFactory {

    private static final Faker faker = new Faker(new Locale("pt", "BR"));

    public Veiculo create(UUID clienteId) {
        String placa = faker.vehicle().licensePlate();
        String marca = faker.vehicle().make();
        String modelo = faker.vehicle().model();
        int ano = faker.number().numberBetween(2010, 2024);

        return new Veiculo(clienteId, placa, marca, modelo, ano);
    }
}






