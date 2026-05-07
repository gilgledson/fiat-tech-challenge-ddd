package br.com.fiap.oficina.api.modules.catalogo.servico.application;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.dto.ServicoOutput;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.mapper.ServicoOutputMapper;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.ProdutoSugerido;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.Servico;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@DisplayName("Serviço Output Mapper - Testes Unitários")
class ServicoOutputMapperTest {

    private ProdutoRepository produtoRepository;
    private ServicoOutputMapper mapper;

    @BeforeEach
    void setUp() {
        produtoRepository = Mockito.mock(ProdutoRepository.class);
        mapper = new ServicoOutputMapper(produtoRepository);
    }

    @Test
    @DisplayName("Deve mapear um serviço para ServicoOutput")
    void deveMapearServico() {
        UUID produtoId = UUID.randomUUID();
        ProdutoSugerido sugerido = new ProdutoSugerido(produtoId, BigDecimal.ONE);
        Servico servico = new Servico("Troca", TipoServico.PREVENTIVO, BigDecimal.TEN, List.of(sugerido));
        
        Produto mockProduto = Mockito.mock(Produto.class);
        when(mockProduto.getId()).thenReturn(produtoId);
        when(mockProduto.getNome()).thenReturn("Óleo");
        when(produtoRepository.buscarPorIds(anyList())).thenReturn(List.of(mockProduto));

        ServicoOutput output = mapper.mapear(servico);

        assertEquals(servico.getId(), output.id());
        assertEquals("Troca", output.nome());
        assertEquals(1, output.produtosSugeridos().size());
        assertEquals("Óleo", output.produtosSugeridos().get(0).nomeProduto());
    }

    @Test
    @DisplayName("Deve mapear lista de serviços")
    void deveMapearLista() {
        Servico servico = new Servico("Troca", TipoServico.PREVENTIVO, BigDecimal.TEN, List.of());
        
        List<ServicoOutput> outputs = mapper.mapearLista(List.of(servico));

        assertEquals(1, outputs.size());
        assertEquals("Troca", outputs.get(0).nome());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando entrada for nula ou vazia")
    void deveRetornarListaVazia() {
        assertTrue(mapper.mapearLista(null).isEmpty());
        assertTrue(mapper.mapearLista(List.of()).isEmpty());
    }
}









