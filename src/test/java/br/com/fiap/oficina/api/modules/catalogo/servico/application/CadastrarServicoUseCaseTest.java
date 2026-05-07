package br.com.fiap.oficina.api.modules.catalogo.servico.application;

import br.com.fiap.oficina.api.modules.catalogo.servico.application.repository.ServicoRepository;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.CadastrarServicoUseCaseImpl;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.dto.ServicoOutput;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.mapper.ServicoOutputMapper;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.validator.ProdutoSugeridoValidator;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.ProdutoSugerido;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CadastrarServicoUseCase - Testes Unitários")
class CadastrarServicoUseCaseTest {

    @Mock
    private ServicoRepository servicoRepository;
    @Mock
    private ProdutoSugeridoValidator produtoSugeridoValidator;
    @Mock
    private ServicoOutputMapper servicoOutputMapper;

    private CadastrarServicoUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CadastrarServicoUseCaseImpl(servicoRepository, produtoSugeridoValidator, servicoOutputMapper);
    }

    @Test
    @DisplayName("Deve salvar serviço com produtos sugeridos")
    void deveCadastrarComProdutosSugeridos() {
        List<ProdutoSugerido> produtos = List.of(new ProdutoSugerido(UUID.randomUUID(), new BigDecimal("4")));
        doNothing().when(produtoSugeridoValidator).validar(produtos);
        when(servicoOutputMapper.mapear(any())).thenReturn(mock(ServicoOutput.class));

        ServicoOutput resultado = useCase.execute("Troca de Óleo", TipoServico.PREVENTIVO,
                new BigDecimal("80.00"), produtos);

        assertNotNull(resultado);
        verify(servicoRepository, times(1)).salvar(any());
        verify(produtoSugeridoValidator, times(1)).validar(produtos);
    }

    @Test
    @DisplayName("Deve salvar serviço sem produtos sugeridos")
    void deveCadastrarSemProdutosSugeridos() {
        List<ProdutoSugerido> produtosVazios = Collections.emptyList();
        doNothing().when(produtoSugeridoValidator).validar(produtosVazios);
        when(servicoOutputMapper.mapear(any())).thenReturn(mock(ServicoOutput.class));

        ServicoOutput resultado = useCase.execute("Serviço Simples", TipoServico.PREVENTIVO,
                new BigDecimal("150.00"), produtosVazios);

        assertNotNull(resultado);
        verify(servicoRepository, times(1)).salvar(any());
    }
}









