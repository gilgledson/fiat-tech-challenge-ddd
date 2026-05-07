package br.com.fiap.oficina.api.modules.catalogo.servico.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Embeddable
@Table(name = "SERVICO_PRODUTO_SUGERIDO")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProdutoSugeridoEmbeddable {

    @Column(name = "produto_id")
    private UUID produtoId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidade;
}






