package br.com.fiap.oficina.api.modules.atendimento.cliente.infrastructure.seeder;

import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.entity.Cliente;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.valueobject.Endereco;
import jakarta.enterprise.context.ApplicationScoped;
import net.datafaker.Faker;

import java.util.Locale;
import java.util.UUID;

@ApplicationScoped
public class ClienteFactory {

    private static final Faker faker = new Faker(new Locale("pt", "BR"));
    private static final String[] BAIRROS_BRASILEIROS = {
            "Centro", "Vila Nova", "Jardim das Flores", "Bela Vista",
            "Jardim Paulista", "Vila Mariana", "Pinheiros", "Mooca",
            "Botafogo", "Copacabana", "Savassi", "Boa Viagem", "Itaim Bibi",
            "Tatuapé", "Vila Leopoldina", "Barra da Tijuca", "Leblon",
            "Jardins", "Mooca", "Vila Madalena", "Campo Belo",
            "Brooklin", "Liberdade", "Bela Vista", "Pompéia",
            "Higienópolis", "Lapa", "Tucuruvi", "Aclimação",
            "Saúde", "Santana", "Santo Amaro", "Butantã",
            "Bixiga", "Cambuci", "Jardim America", "Vila Olímpia",
            "Pinheiros", "Itaim Bibi", "Vila Mariana", "Mooca",
            "Botafogo", "Copacabana", "Savassi", "Boa Viagem", "Itaim Bibi"
    };

    private static final String[] COMPLEMENTOS = {
            "Apto 101", "Casa 2", "Fundos", "Bloco B, Apto 402",
            "Sala 3", "Esquina com a padaria", "", "", "", "Casa dos Fundos",
            "Apartamento 302", "Bloco A", "Sala Comercial 10",
            "Casa geminada", "Cobertura", "Loft", "Condomínio Fechado",
            "Vilagio", "Sobrado", "Condomínio", "Travessa", "Alameda",
            "Condomínio Fechado", "Vilagio", "Sobrado", "Condomínio", "Travessa",
            "Alameda", "Condomínio Fechado", "Vilagio", "Sobrado", "Condomínio",
            "Travessa", "Alameda", "Condomínio Fechado", "Vilagio", "Sobrado",
            "Condomínio", "Travessa", "Alameda", "Condomínio Fechado",
            "Vilagio", "Sobrado", "Condomínio", "Travessa", "Alameda"
    };

    public Cliente create(UUID usuarioId) {
        String nome = faker.name().fullName();
        String email = faker.internet().emailAddress();

        // Formato simplificado para o seeder passar na validacao basica
        String cpfCnpj = faker.cpf().valid();
        String telefone = faker.phoneNumber().phoneNumber();

        Endereco endereco = new Endereco(
                faker.address().zipCode(),
                faker.address().streetName(),
                faker.address().buildingNumber(),
                faker.options().option(COMPLEMENTOS),
                faker.options().option(BAIRROS_BRASILEIROS),
                faker.address().cityName(),
                faker.address().stateAbbr());

        return new Cliente(usuarioId, nome, email, cpfCnpj, telefone, endereco);
    }
}






