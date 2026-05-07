package br.com.fiap.oficina.api.modules.catalogo.servico.infrastructure.persistence;

import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "SERVICO")
@Getter
@Setter
public class ServicoJpaEntity {
    @Id
    private UUID id;

    @Column
    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoServico tipo;

    @Column(name = "preco_base")
    private BigDecimal precoBase;

    @Column(name = "deletado_em")
    private LocalDateTime deletadoEm;

    @ElementCollection
    @CollectionTable(name = "SERVICO_PRODUTO_SUGERIDO", joinColumns = @JoinColumn(name = "servico_id"))
    private List<ProdutoSugeridoEmbeddable> produtosSugeridos = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ServicoJpaEntity))
            return false;
        ServicoJpaEntity that = (ServicoJpaEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}






