package br.com.fiap.oficina.api.modules.atendimento.veiculo.application;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.dto.VeiculoOutput;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.usecase.ListarVeiculosUseCaseImpl;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.domain.entity.Veiculo;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DisplayName("Listar Veículos - Testes Unitários")
class ListarVeiculosUseCaseTest {

    private VeiculoRepository repository;
    private ListarVeiculosUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(VeiculoRepository.class);
        useCase = new ListarVeiculosUseCaseImpl(repository);
    }

    @Test
    @DisplayName("Deve listar veículos paginados")
    void deveListarVeiculos() {
        Veiculo veiculo = new Veiculo(UUID.randomUUID(), "ABC-1234", "Fiat", "Palio", 2010);
        Pagina<Veiculo> paginaMock = new Pagina<>(List.of(veiculo), 0, 10, 1, 1);
        
        when(repository.listarTodos(anyInt(), anyInt(), anyBoolean())).thenReturn(paginaMock);

        Pagina<VeiculoOutput> resultado = useCase.executar(0, 10, false);

        assertEquals(1, resultado.itens().size());
        assertEquals("ABC-1234", resultado.itens().get(0).placa());
        assertEquals(0, resultado.paginaAtual());
        assertEquals(1, resultado.totalElementos());
    }
}









