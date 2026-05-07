package br.com.fiap.oficina.api.modules.catalogo.servico.application;

import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.dto.ServicoOutput;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.repository.ServicoRepository;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.EditarServicoUseCaseImpl;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.mapper.ServicoOutputMapper;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.validator.ProdutoSugeridoValidator;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.Servico;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Editar Serviço - Testes Unitários")
class EditarServicoUseCaseTest {

    private ServicoRepository repository;
    private ServicoOutputMapper mapper;
    private ProdutoSugeridoValidator validator;
    private EditarServicoUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ServicoRepository.class);
        mapper = Mockito.mock(ServicoOutputMapper.class);
        validator = Mockito.mock(ProdutoSugeridoValidator.class);
        useCase = new EditarServicoUseCaseImpl(repository, mapper, validator);
    }

    @Test
    @DisplayName("Deve editar um serviço com sucesso")
    void deveEditarServico() {
        UUID id = UUID.randomUUID();
        Servico servico = new Servico("Antigo", TipoServico.CORRETIVO, BigDecimal.ONE, null);
        
        when(repository.buscarPorId(id)).thenReturn(Optional.of(servico));
        when(mapper.mapear(any(Servico.class))).thenReturn(Mockito.mock(ServicoOutput.class));

        useCase.executar(id, "Novo", TipoServico.PREVENTIVO, BigDecimal.TEN, new ArrayList<>());

        assertEquals("Novo", servico.getNome());
        assertEquals(TipoServico.PREVENTIVO, servico.getTipo());
        verify(repository).atualizar(servico);
    }

    @Test
    @DisplayName("Deve lançar erro quando serviço não existe")
    void deveLancarErroInexistente() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> 
            useCase.executar(id, "Novo", TipoServico.PREVENTIVO, BigDecimal.TEN, new ArrayList<>())
        );
    }
}









