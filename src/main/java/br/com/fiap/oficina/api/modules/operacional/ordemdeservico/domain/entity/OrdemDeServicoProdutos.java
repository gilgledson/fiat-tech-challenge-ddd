package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdemDeServicoProdutos {
    private UUID produtoId;
    private UUID ordemDeServicoId;
    private BigDecimal quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal total;
    private String nomeDoProduto;

    public BigDecimal total() {
        return this.precoUnitario.multiply(this.quantidade);
    }
}






