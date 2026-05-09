package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado;

import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto.AprovarServicoRequest;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicos;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoProdutoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoProdutos;
import io.vertx.core.eventbus.EventBus;
import jakarta.ws.rs.NotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AprovarOrdemDeServicoUseCaseTest {

    @Mock
    private OrdemDeServicoRepository repository;

    @Mock
    private EventBus eventBus;

    @Mock
    private CatalogoProdutoGateway produtoGateway;

    @InjectMocks
    private AprovarOrdemDeServicoUseCaseImpl useCase;

    @Test
    @DisplayName("Deve lançar exceção ao tentar rejeitar um serviço corretivo")
    void deveLancarExcecaoAoRejeitarServicoCorretivo() {
        // Arrange
        UUID osId = UUID.randomUUID();
        UUID servicoCorretivoId = UUID.randomUUID();

        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.AGUARDANDO_APROVACAO);

        List<OrdemDeServicoServicos> servicos = new ArrayList<>();
        OrdemDeServicoServicos servicoCorretivo = new OrdemDeServicoServicos(osId, servicoCorretivoId, "Freio", 1,
                BigDecimal.TEN, BigDecimal.TEN, OrdemDeServicoServicoStatus.PENDENTE, TipoServico.CORRETIVO);
        servicos.add(servicoCorretivo);
        os.setServicos(servicos);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        AprovarServicoRequest request = new AprovarServicoRequest(
                List.of(), // aprovados
                List.of(servicoCorretivo.getId()) // rejeitados - ID da instância, não do catálogo
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.executar(osId, request);
        });

        assertTrue(exception.getMessage().contains("não pode ser rejeitado"));
    }

    @Test
    @DisplayName("Deve permitir aprovação de serviço extra quando a OS já está em execução")
    void devePermitirAprovacaoDeServicoExtraEmExecucao() {
        // Arrange
        UUID osId = UUID.randomUUID();
        UUID servicoExtraId = UUID.randomUUID();

        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.EM_EXECUCAO);

        List<OrdemDeServicoServicos> servicos = new ArrayList<>();
        // Serviço já aprovado e em execução
        servicos.add(new OrdemDeServicoServicos(osId, UUID.randomUUID(), "Serviço 1", 1, BigDecimal.TEN, BigDecimal.TEN,
                OrdemDeServicoServicoStatus.EM_EXECUCAO, TipoServico.PREVENTIVO));
        // Serviço extra pendente
        OrdemDeServicoServicos servicoExtra = new OrdemDeServicoServicos(osId, servicoExtraId, "Serviço Extra", 1,
                BigDecimal.TEN, BigDecimal.TEN, OrdemDeServicoServicoStatus.PENDENTE, TipoServico.PREVENTIVO);
        servicos.add(servicoExtra);
        os.setServicos(servicos);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        AprovarServicoRequest request = new AprovarServicoRequest(
                List.of(servicoExtra.getId()), // ID da instância, não do catálogo
                List.of());

        // Act
        useCase.executar(osId, request);

        // Assert
        assertEquals(OrdemDeServicoServicoStatus.APROVADO, servicoExtra.getStatus());
        assertEquals(OrdemDeServicoStatus.EM_EXECUCAO, os.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar aprovar OS em status inválido")
    void deveLancarExcecaoAoAprovarOsEmStatusInvalido() {
        // Arrange
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setStatus(OrdemDeServicoStatus.ENTREGUE);
        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            useCase.executar(osId, new AprovarServicoRequest(List.of(), List.of()));
        });
    }

    @Test
    @DisplayName("Deve lançar exceção quando a OS não existe")
    void deveLancarExcecaoQuandoOsNaoExiste() {
        // Arrange
        UUID osId = UUID.randomUUID();
        when(repository.buscarPorId(osId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            useCase.executar(osId, new AprovarServicoRequest(List.of(), List.of()));
        });
    }

    @Test
    @DisplayName("Deve processar aprovação com listas nulas")
    void deveProcessarComListasNulas() {
        // Arrange
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.AGUARDANDO_APROVACAO);
        os.setServicos(new ArrayList<>());
        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        // Act
        useCase.executar(osId, new AprovarServicoRequest(null, null));

        // Assert
        verify(repository).atualizar(os);
    }

    @Test
    @DisplayName("Deve liberar estoque ao rejeitar serviço com produtos")
    void deveLiberarEstoqueAoRejeitarServicoComProdutos() {
        // Arrange
        UUID osId = UUID.randomUUID();
        UUID servicoId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();

        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.AGUARDANDO_APROVACAO);

        OrdemDeServicoServicos servico = new OrdemDeServicoServicos(osId, servicoId, "Serviço", 1, BigDecimal.TEN,
                BigDecimal.TEN, OrdemDeServicoServicoStatus.PENDENTE, TipoServico.PREVENTIVO);
        OrdemDeServicoProdutos produto = new OrdemDeServicoProdutos(produtoId, osId, BigDecimal.valueOf(2),
                BigDecimal.TEN, BigDecimal.valueOf(20), "Produto");
        servico.setProdutos(List.of(produto));
        os.setServicos(List.of(servico));

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        AprovarServicoRequest request = new AprovarServicoRequest(null, List.of(servico.getId()));

        // Act
        useCase.executar(osId, request);

        // Assert
        assertEquals(OrdemDeServicoServicoStatus.CANCELADO, servico.getStatus());
        verify(produtoGateway).liberarEstoqueReservado(produtoId, BigDecimal.valueOf(2));
    }

    @Test
    @DisplayName("Deve rejeitar a OS quando nenhum serviço é aprovado")
    void deveRejeitarOsQuandoNenhumServicoAprovado() {
        // Arrange
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.AGUARDANDO_APROVACAO);
        os.setServicos(new ArrayList<>());
        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        // Act
        useCase.executar(osId, new AprovarServicoRequest(List.of(), List.of()));

        // Assert
        assertEquals(OrdemDeServicoStatus.REJEITADA, os.getStatus());
        verify(eventBus).publish(any(), any());
    }
}
