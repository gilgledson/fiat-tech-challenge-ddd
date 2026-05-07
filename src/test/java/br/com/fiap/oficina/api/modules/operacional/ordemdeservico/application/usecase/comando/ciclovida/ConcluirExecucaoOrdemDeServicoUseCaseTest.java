package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.ciclovida;

import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoProdutoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.*;
import io.vertx.core.eventbus.EventBus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcluirExecucaoOrdemDeServicoUseCaseTest {

    @Mock
    private OrdemDeServicoRepository repository;

    @Mock
    private CatalogoProdutoGateway produtoGateway;

    @Mock
    private EventBus eventBus;

    @InjectMocks
    private ConcluirExecucaoOrdemDeServicoUseCaseImpl useCase;

    @Test
    @DisplayName("Deve concluir a execução da OS quando todos os serviços estiverem finalizados")
    void deveConcluirExecucaoComSucesso() {
        // Arrange
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setClienteId(UUID.randomUUID());
        os.setStatus(OrdemDeServicoStatus.EM_EXECUCAO);
        
        List<OrdemDeServicoServicos> servicos = new ArrayList<>();
        servicos.add(new OrdemDeServicoServicos(osId, UUID.randomUUID(), "Servico 1", 1, BigDecimal.TEN, BigDecimal.TEN, OrdemDeServicoServicoStatus.FINALIZADO, TipoServico.PREVENTIVO));
        os.setServicos(servicos);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        // Act
        useCase.executar(osId);

        // Assert
        assertEquals(OrdemDeServicoStatus.AGUARDANDO_PAGAMENTO, os.getStatus());
        verify(repository).atualizar(os);
        verify(eventBus).publish(anyString(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando houver serviços não finalizados")
    void deveLancarExcecaoQuandoHouverServicosPendentes() {
        // Arrange
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.EM_EXECUCAO);
        
        List<OrdemDeServicoServicos> servicos = new ArrayList<>();
        servicos.add(new OrdemDeServicoServicos(osId, UUID.randomUUID(), "Servico 1", 1, BigDecimal.TEN, BigDecimal.TEN, OrdemDeServicoServicoStatus.EM_EXECUCAO, TipoServico.CORRETIVO));
        os.setServicos(servicos);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.executar(osId));
        assertEquals("A ordem não pode ser finalizada enquanto houver serviços pendentes ou em execução.", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a OS não estiver EM_EXECUCAO")
    void deveLancarExcecaoQuandoStatusInvalido() {
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.ABERTA);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        assertThrows(IllegalArgumentException.class, () -> useCase.executar(osId));
    }
}









