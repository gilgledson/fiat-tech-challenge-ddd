package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity;

import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrdemDeServicoTest {

    @Test
    @DisplayName("Deve calcular o valor total da OS incluindo apenas serviços aprovados, em execução ou finalizados")
    void deveCalcularValorTotalCorretamente() {
        // Arrange
        OrdemDeServico os = new OrdemDeServico();
        
        List<OrdemDeServicoProdutos> produtos = new ArrayList<>();
        produtos.add(new OrdemDeServicoProdutos(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("2"), new BigDecimal("50.00"), new BigDecimal("100.00"), "Produto 1"));
        os.setProdutos(produtos);

        List<OrdemDeServicoServicos> servicos = new ArrayList<>();
        
        // Serviço Aprovado - Deve somar
        servicos.add(new OrdemDeServicoServicos(UUID.randomUUID(), UUID.randomUUID(), "Servico 1", 1, new BigDecimal("100.00"), new BigDecimal("100.00"), OrdemDeServicoServicoStatus.APROVADO, TipoServico.PREVENTIVO));
        
        // Serviço Em Execução - Deve somar
        servicos.add(new OrdemDeServicoServicos(UUID.randomUUID(), UUID.randomUUID(), "Servico 2", 1, new BigDecimal("150.00"), new BigDecimal("150.00"), OrdemDeServicoServicoStatus.EM_EXECUCAO, TipoServico.CORRETIVO));
        
        // Serviço Finalizado - Deve somar
        servicos.add(new OrdemDeServicoServicos(UUID.randomUUID(), UUID.randomUUID(), "Servico 3", 1, new BigDecimal("200.00"), new BigDecimal("200.00"), OrdemDeServicoServicoStatus.FINALIZADO, TipoServico.PREVENTIVO));
        
        // Serviço Pendente - NÃO deve somar
        servicos.add(new OrdemDeServicoServicos(UUID.randomUUID(), UUID.randomUUID(), "Servico 4", 1, new BigDecimal("300.00"), new BigDecimal("300.00"), OrdemDeServicoServicoStatus.PENDENTE, TipoServico.CORRETIVO));
        
        // Serviço Rejeitado - NÃO deve somar
        servicos.add(new OrdemDeServicoServicos(UUID.randomUUID(), UUID.randomUUID(), "Servico 5", 1, new BigDecimal("500.00"), new BigDecimal("500.00"), OrdemDeServicoServicoStatus.REJEITADO, TipoServico.PREVENTIVO));
        
        os.setServicos(servicos);

        // Act
        BigDecimal valorTotal = os.calcularValorTotal();

        // Assert
        // Produtos (100) + Servicos (100 + 150 + 200) = 550
        assertEquals(new BigDecimal("550.00"), valorTotal);
    }

    @Test
    @DisplayName("Deve calcular valor total como zero quando não houver itens")
    void deveCalcularValorTotalComoZero() {
        OrdemDeServico os = new OrdemDeServico();
        os.setProdutos(List.of());
        os.setServicos(List.of());

        assertEquals(BigDecimal.ZERO, os.calcularValorTotal());
    }
}
