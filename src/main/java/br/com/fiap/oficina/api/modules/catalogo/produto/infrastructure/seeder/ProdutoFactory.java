package br.com.fiap.oficina.api.modules.catalogo.produto.infrastructure.seeder;

import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.UnidadeMedida;
import jakarta.enterprise.context.ApplicationScoped;
import net.datafaker.Faker;

import java.math.BigDecimal;

@ApplicationScoped
public class ProdutoFactory {

    private final Faker faker = new Faker();

    public Produto create() {
        String nome = faker.commerce().productName();
        String codigoBarras = faker.code().ean13();
        BigDecimal preco = BigDecimal.valueOf(faker.number().randomDouble(2, 10, 500));
        BigDecimal estoque = BigDecimal.valueOf(faker.number().randomDouble(2, 0, 100));
        BigDecimal reservado = BigDecimal.ZERO;
        
        return new Produto(nome, codigoBarras, preco, estoque, reservado, UnidadeMedida.UN);
    }
}






