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
    @DisplayName("Deve calcular o valor total da OS incluindo apenas serviços aprovados (com seus produtos), em execução ou finalizados")
    void deveCalcularValorTotalCorretamente() {
        // Arrange
        OrdemDeServico os = new OrdemDeServico();
        List<OrdemDeServicoServicos> servicos = new ArrayList<>();
        
        // Serviço Aprovado (100) + Produto (50) - Deve somar 150
        OrdemDeServicoServicos s1 = new OrdemDeServicoServicos(UUID.randomUUID(), UUID.randomUUID(), "Servico 1", 1, new BigDecimal("100.00"), null, OrdemDeServicoServicoStatus.APROVADO, TipoServico.PREVENTIVO);
        s1.getProdutos().add(new OrdemDeServicoProdutos(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("1"), new BigDecimal("50.00"), new BigDecimal("50.00"), "Produto 1"));
        s1.calcularTotal();
        servicos.add(s1);
        
        // Serviço Em Execução (150) - Deve somar 150
        OrdemDeServicoServicos s2 = new OrdemDeServicoServicos(UUID.randomUUID(), UUID.randomUUID(), "Servico 2", 1, new BigDecimal("150.00"), new BigDecimal("150.00"), OrdemDeServicoServicoStatus.EM_EXECUCAO, TipoServico.CORRETIVO);
        servicos.add(s2);
        
        // Serviço Finalizado (200) - Deve somar 200
        OrdemDeServicoServicos s3 = new OrdemDeServicoServicos(UUID.randomUUID(), UUID.randomUUID(), "Servico 3", 1, new BigDecimal("200.00"), new BigDecimal("200.00"), OrdemDeServicoServicoStatus.FINALIZADO, TipoServico.PREVENTIVO);
        servicos.add(s3);
        
        // Serviço Pendente (300) - NÃO deve somar
        OrdemDeServicoServicos s4 = new OrdemDeServicoServicos(UUID.randomUUID(), UUID.randomUUID(), "Servico 4", 1, new BigDecimal("300.00"), new BigDecimal("300.00"), OrdemDeServicoServicoStatus.PENDENTE, TipoServico.CORRETIVO);
        servicos.add(s4);
        
        // Serviço Rejeitado (500) - NÃO deve somar
        OrdemDeServicoServicos s5 = new OrdemDeServicoServicos(UUID.randomUUID(), UUID.randomUUID(), "Servico 5", 1, new BigDecimal("500.00"), new BigDecimal("500.00"), OrdemDeServicoServicoStatus.REJEITADO, TipoServico.PREVENTIVO);
        servicos.add(s5);
        
        os.setServicos(servicos);

        // Act
        BigDecimal valorTotal = os.calcularValorTotal();

        // Assert
        // Aprovado (150) + Em Execução (150) + Finalizado (200) = 500
        assertEquals(new BigDecimal("500.00"), valorTotal);
    }

    @Test
    @DisplayName("Deve calcular valor total como zero quando não houver itens")
    void deveCalcularValorTotalComoZero() {
        OrdemDeServico os = new OrdemDeServico();
        os.setServicos(List.of());

        assertEquals(BigDecimal.ZERO, os.calcularValorTotal());
    }
}









