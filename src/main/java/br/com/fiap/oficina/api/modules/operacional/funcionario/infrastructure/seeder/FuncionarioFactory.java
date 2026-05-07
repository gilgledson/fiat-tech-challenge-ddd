package br.com.fiap.oficina.api.modules.operacional.funcionario.infrastructure.seeder;

import br.com.fiap.oficina.api.modules.operacional.funcionario.domain.entity.Funcionario;
import br.com.fiap.oficina.api.modules.operacional.funcionario.domain.valueObject.CargoFuncionario;
import jakarta.enterprise.context.ApplicationScoped;
import net.datafaker.Faker;

import java.util.Locale;
import java.util.UUID;

@ApplicationScoped
public class FuncionarioFactory {

    private static final Faker faker = new Faker(new Locale("pt", "BR"));

    public Funcionario create(UUID usuarioId, CargoFuncionario cargo) {
        String nome = faker.name().firstName();
        String sobrenome = faker.name().lastName();
        String cpf = faker.cpf().valid();
        String telefone = faker.regexify("\\([1-9]{2}\\) 9[0-9]{4}-[0-9]{4}");

        return new Funcionario(usuarioId, nome, sobrenome, cpf, telefone, cargo);
    }
}






