package br.com.fiap.oficina.api.modules.catalogo.servico.application;

import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.dto.ServicoOutput;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.repository.ServicoRepository;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.ListarServicosUseCaseImpl;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.mapper.ServicoOutputMapper;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.Servico;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DisplayName("Listar Serviços - Testes Unitários")
class ListarServicosUseCaseTest {

    private ServicoRepository repository;
    private ServicoOutputMapper mapper;
    private ListarServicosUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ServicoRepository.class);
        mapper = Mockito.mock(ServicoOutputMapper.class);
        useCase = new ListarServicosUseCaseImpl(repository, mapper);
    }

    @Test
    @DisplayName("Deve listar serviços paginados")
    void deveListarServicos() {
        Servico servico = new Servico("Troca", TipoServico.PREVENTIVO, BigDecimal.TEN, null);
        Pagina<Servico> paginaMock = new Pagina<>(List.of(servico), 0, 10, 1, 1);
        
        when(repository.listarTodos(anyInt(), anyInt(), anyBoolean())).thenReturn(paginaMock);
        when(mapper.mapearLista(anyList())).thenReturn(List.of(Mockito.mock(ServicoOutput.class)));

        Pagina<ServicoOutput> resultado = useCase.executar(0, 10, false);

        assertEquals(1, resultado.itens().size());
        assertEquals(0, resultado.paginaAtual());
    }
}









