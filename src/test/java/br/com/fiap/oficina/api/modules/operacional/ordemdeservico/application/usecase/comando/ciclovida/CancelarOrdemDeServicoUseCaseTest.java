package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.ciclovida;

import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoProdutoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.*;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelarOrdemDeServicoUseCaseTest {

    @Mock
    private OrdemDeServicoRepository repository;

    @Mock
    private CatalogoProdutoGateway produtoGateway;

    @Mock
    private EventBus eventBus;

    @InjectMocks
    private CancelarOrdemDeServicoUseCaseImpl useCase;

    @Test
    @DisplayName("Deve cancelar OS e liberar estoque reservado dos produtos")
    void deveCancelarOsELiberarEstoque() {
        UUID osId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();

        OrdemDeServicoServicos servico = new OrdemDeServicoServicos(
                osId, UUID.randomUUID(), "Troca de óleo", 1,
                BigDecimal.TEN, BigDecimal.TEN, OrdemDeServicoServicoStatus.APROVADO, TipoServico.PREVENTIVO);

        OrdemDeServicoProdutos produto = new OrdemDeServicoProdutos();
        produto.setProdutoId(produtoId);
        produto.setQuantidade(new BigDecimal("2"));
        servico.getProdutos().add(produto);

        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.EM_EXECUCAO);
        os.setServicos(new ArrayList<>(List.of(servico)));

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        useCase.executar(osId, "Cliente desistiu");

        assertEquals(OrdemDeServicoStatus.CANCELADA, os.getStatus());
        assertEquals("Cliente desistiu", os.getMotivoCancelamento());
        assertNotNull(os.getDeletadoEm());
        verify(produtoGateway).liberarEstoqueReservado(produtoId, new BigDecimal("2"));
        verify(repository).atualizar(os);
        verify(eventBus).publish(anyString(), any());
    }

    @Test
    @DisplayName("Deve cancelar OS sem serviços sem chamar gateway de produto")
    void deveCancelarOsSemServicos() {
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.ABERTA);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        useCase.executar(osId, "Engano");

        assertEquals(OrdemDeServicoStatus.CANCELADA, os.getStatus());
        verifyNoInteractions(produtoGateway);
        verify(repository).atualizar(os);
    }

    @Test
    @DisplayName("Deve lançar exceção quando OS não for encontrada")
    void deveLancarExcecaoQuandoOsNaoEncontrada() {
        UUID osId = UUID.randomUUID();
        when(repository.buscarPorId(osId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.executar(osId, "motivo"));
        verify(repository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar OS já encerrada")
    void deveLancarExcecaoQuandoOsJaEncerrada() {
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.ENTREGUE);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.executar(osId, "motivo"));
        assertTrue(ex.getMessage().contains("cancelar"));
        verify(repository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar OS já CANCELADA")
    void deveLancarExcecaoQuandoOsJaCancelada() {
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.CANCELADA);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        assertThrows(IllegalArgumentException.class, () -> useCase.executar(osId, "motivo"));
    }
}









