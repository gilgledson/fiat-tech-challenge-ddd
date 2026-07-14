package br.com.fiap.oficina.api.shared.infrastructure.seeder;

public interface Seeder {
    void execute();

    /**
     * Define a ordem de execução entre seeders (menor valor executa primeiro).
     * Necessário porque seeders que dependem de dados de outros módulos (ex.:
     * OrdemServicoSeeder precisa de clientes, veículos, produtos e serviços já
     * existentes) não podem confiar na ordem de iteração do CDI, que não é garantida.
     */
    default int ordem() {
        return 0;
    }
}






