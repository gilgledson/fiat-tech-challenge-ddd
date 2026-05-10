package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item;

import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto.AdicionarServicoRequest;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto.ProdutoItemRequest;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.ProdutoSnapshotDTO;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.ServicoSnapshotDTO;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoProdutoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoServicoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdicionarServicoOrdemDeServicoUseCaseTest {

    @Mock
    private OrdemDeServicoRepository osRepository;

    @Mock
    private CatalogoServicoGateway servicoGateway;

    @Mock
    private CatalogoProdutoGateway produtoGateway;

    @Mock
    private EventBus eventBus;

    @InjectMocks
    private AdicionarServicoOrdemDeServicoUseCaseImpl useCase;

    private OrdemDeServico criarOs(OrdemDeServicoStatus status) {
        OrdemDeServico os = new OrdemDeServico();
        os.setId(UUID.randomUUID());
        os.setClienteId(UUID.randomUUID());
        os.setStatus(status);
        return os;
    }

    @Test
    @DisplayName("Deve adicionar serviço sem produtos com UUID de instância único")
    void deveAdicionarServicoSemProdutos() {
        UUID osId = UUID.randomUUID();
        UUID catalogoServicoId = UUID.randomUUID();

        OrdemDeServico os = criarOs(OrdemDeServicoStatus.EM_DIAGNOSTICO);
        os.setId(osId);

        ServicoSnapshotDTO snapshot = new ServicoSnapshotDTO(catalogoServicoId, "Alinhamento",
                new BigDecimal("120.00"), TipoServico.PREVENTIVO);

        when(osRepository.buscarPorId(osId)).thenReturn(Optional.of(os));
        when(servicoGateway.buscarPorId(catalogoServicoId)).thenReturn(Optional.of(snapshot));

        AdicionarServicoRequest request = new AdicionarServicoRequest(catalogoServicoId, 1, List.of());

        OrdemDeServicoOutput resultado = useCase.executar(osId, request);

        assertEquals(1, os.getServicos().size());
        OrdemDeServicoServicos adicionado = os.getServicos().get(0);
        assertNotNull(adicionado.getId());
        assertNotEquals(catalogoServicoId, adicionado.getId());
        assertEquals(catalogoServicoId, adicionado.getServicoId());
        assertEquals(OrdemDeServicoServicoStatus.PENDENTE, adicionado.getStatus());
        verify(osRepository).atualizar(os);
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("Deve adicionar serviço com produto, reservar estoque e calcular total")
    void deveAdicionarServicoComProduto() {
        UUID osId = UUID.randomUUID();
        UUID catalogoServicoId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();

        OrdemDeServico os = criarOs(OrdemDeServicoStatus.EM_DIAGNOSTICO);
        os.setId(osId);

        ServicoSnapshotDTO servicoSnapshot = new ServicoSnapshotDTO(catalogoServicoId, "Troca de óleo",
                new BigDecimal("80.00"), TipoServico.PREVENTIVO);
        ProdutoSnapshotDTO produtoSnapshot = new ProdutoSnapshotDTO(produtoId, "Óleo 5W30",
                new BigDecimal("45.00"));

        when(osRepository.buscarPorId(osId)).thenReturn(Optional.of(os));
        when(servicoGateway.buscarPorId(catalogoServicoId)).thenReturn(Optional.of(servicoSnapshot));
        when(produtoGateway.buscarPorId(produtoId)).thenReturn(Optional.of(produtoSnapshot));

        ProdutoItemRequest produtoRequest = new ProdutoItemRequest(produtoId, new BigDecimal("2"));
        AdicionarServicoRequest request = new AdicionarServicoRequest(catalogoServicoId, 1,
                List.of(produtoRequest));

        useCase.executar(osId, request);

        OrdemDeServicoServicos adicionado = os.getServicos().get(0);
        assertEquals(1, adicionado.getProdutos().size());
        verify(produtoGateway).reservarEstoque(produtoId, new BigDecimal("2"));
        verify(osRepository).atualizar(os);
    }

    @Test
    @DisplayName("Deve lançar exceção quando OS não for encontrada")
    void deveLancarExcecaoQuandoOsNaoEncontrada() {
        UUID osId = UUID.randomUUID();
        when(osRepository.buscarPorId(osId)).thenReturn(Optional.empty());

        AdicionarServicoRequest request = new AdicionarServicoRequest(UUID.randomUUID(), 1, List.of());

        assertThrows(NotFoundException.class, () -> useCase.executar(osId, request));
        verify(osRepository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando OS estiver encerrada")
    void deveLancarExcecaoQuandoOsEncerrada() {
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = criarOs(OrdemDeServicoStatus.CANCELADA);
        os.setId(osId);

        when(osRepository.buscarPorId(osId)).thenReturn(Optional.of(os));

        AdicionarServicoRequest request = new AdicionarServicoRequest(UUID.randomUUID(), 1, List.of());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.executar(osId, request));
        assertTrue(ex.getMessage().contains("encerrada"));
        verify(osRepository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando serviço não existir no catálogo")
    void deveLancarExcecaoQuandoServicoCatalogoNaoEncontrado() {
        UUID osId = UUID.randomUUID();
        UUID catalogoServicoId = UUID.randomUUID();
        OrdemDeServico os = criarOs(OrdemDeServicoStatus.EM_DIAGNOSTICO);
        os.setId(osId);

        when(osRepository.buscarPorId(osId)).thenReturn(Optional.of(os));
        when(servicoGateway.buscarPorId(catalogoServicoId)).thenReturn(Optional.empty());

        AdicionarServicoRequest request = new AdicionarServicoRequest(catalogoServicoId, 1, List.of());

        assertThrows(NotFoundException.class, () -> useCase.executar(osId, request));
        verify(osRepository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não existir no catálogo")
    void deveLancarExcecaoQuandoProdutoCatalogoNaoEncontrado() {
        UUID osId = UUID.randomUUID();
        UUID catalogoServicoId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();
        OrdemDeServico os = criarOs(OrdemDeServicoStatus.EM_DIAGNOSTICO);
        os.setId(osId);

        ServicoSnapshotDTO servicoSnapshot = new ServicoSnapshotDTO(catalogoServicoId, "Serviço",
                BigDecimal.TEN, TipoServico.CORRETIVO);

        when(osRepository.buscarPorId(osId)).thenReturn(Optional.of(os));
        when(servicoGateway.buscarPorId(catalogoServicoId)).thenReturn(Optional.of(servicoSnapshot));
        when(produtoGateway.buscarPorId(produtoId)).thenReturn(Optional.empty());

        ProdutoItemRequest produtoRequest = new ProdutoItemRequest(produtoId, BigDecimal.ONE);
        AdicionarServicoRequest request = new AdicionarServicoRequest(catalogoServicoId, 1,
                List.of(produtoRequest));

        assertThrows(NotFoundException.class, () -> useCase.executar(osId, request));
        verify(osRepository, never()).atualizar(any());
    }
}









