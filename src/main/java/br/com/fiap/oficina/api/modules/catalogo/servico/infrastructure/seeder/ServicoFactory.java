package br.com.fiap.oficina.api.modules.catalogo.servico.infrastructure.seeder;

import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.Servico;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import jakarta.enterprise.context.ApplicationScoped;
import net.datafaker.Faker;

import java.math.BigDecimal;
import java.util.ArrayList;

@ApplicationScoped
public class ServicoFactory {

    private final Faker faker = new Faker();

    private static final String[] CATALOGO_DE_SERVICOS = {
            // Manutenção Preventiva e Revisão
            "Troca de Óleo e Filtro de Óleo",
            "Revisão Geral 10.000km",
            "Revisão Geral 50.000km",
            "Substituição do Filtro de Ar do Motor",
            "Substituição do Filtro de Combustível",
            "Substituição do Filtro de Cabine (Ar Condicionado)",

            // Freios
            "Troca das Pastilhas de Freio",
            "Troca dos Discos de Freio",
            "Troca de Lonas e Tambores de Freio",
            "Troca do Fluido de Freio",
            "Sangria do Sistema de Freio",
            "Manutenção do Sistema ABS",

            // Suspensão e Direção
            "Alinhamento e Balanceamento 3D",
            "Troca de Amortecedores Dianteiros e Traseiros",
            "Substituição de Bandejas da Suspensão",
            "Troca de Buchas da Suspensão",
            "Troca de Pivôs e Terminais de Direção",
            "Troca do Fluido da Direção Hidráulica",
            "Reparo na Caixa de Direção",
            "Cambagem e Caster",

            // Motor e Injeção
            "Substituição da Correia Dentada e Tensores",
            "Limpeza de Bicos Injetores (Ultrassom)",
            "Descarbonização do Motor",
            "Retífica de Cabeçote",
            "Troca do Coxim do Motor",
            "Troca da Junta da Tampa de Válvulas",
            "Regulagem de Válvulas",
            "Troca da Correia do Alternador (Poly-V)",

            // Sistema de Arrefecimento
            "Revisão do Sistema de Arrefecimento",
            "Troca do Líquido de Arrefecimento (Aditivo)",
            "Limpeza Completa do Radiador",
            "Troca da Bomba D'água",
            "Substituição da Válvula Termostática",

            // Elétrica e Ignição
            "Escaneamento Eletrônico (Diagnóstico OBD2)",
            "Troca de Velas de Ignição",
            "Troca dos Cabos de Vela",
            "Substituição da Bobina de Ignição",
            "Reparo no Motor de Arranque",
            "Reparo no Alternador",
            "Troca da Bateria 12V",
            "Troca de Lâmpadas (Farol, Lanterna, Seta)",
            "Revisão Geral da Parte Elétrica",

            // Ar Condicionado
            "Higienização do Ar Condicionado (Ozônio)",
            "Carga de Gás do Ar Condicionado",
            "Reparo no Compressor do Ar Condicionado",

            // Transmissão e Escapamento
            "Substituição do Kit de Embreagem",
            "Troca do Óleo da Caixa de Câmbio (Manual/Automático)",
            "Troca do Coxim do Câmbio",
            "Substituição do Silencioso (Escapamento)",
            "Troca do Catalisador"
    };

    public Servico create(TipoServico tipo) {
        String nome = faker.options().option(CATALOGO_DE_SERVICOS);
        BigDecimal preco = BigDecimal.valueOf(faker.number().randomDouble(2, 50, 1000));

        return new Servico(nome, tipo, preco, new ArrayList<>());
    }
}






