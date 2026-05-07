package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item;

import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoProdutoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.*;
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
class RemoverProdutoOrdemDeServicoUseCaseTest {

    @Mock
    private OrdemDeServicoRepository osRepository;

    @Mock
    private CatalogoProdutoGateway produtoGateway;

    @InjectMocks
    private RemoverProdutoOrdemDeServicoUseCaseImpl useCase;

    private OrdemDeServico criarOsComProduto(UUID osId, UUID produtoId, BigDecimal quantidade) {
        OrdemDeServicoProdutos produto = new OrdemDeServicoProdutos();
        produto.setProdutoId(produtoId);
        produto.setQuantidade(quantidade);
        produto.setPrecoUnitario(BigDecimal.TEN);
        produto.setOrdemDeServicoId(osId);

        OrdemDeServicoServicos servico = new OrdemDeServicoServicos(
                osId, UUID.randomUUID(), "Revisão completa", 1,
                BigDecimal.TEN, BigDecimal.TEN, OrdemDeServicoServicoStatus.PENDENTE, TipoServico.PREVENTIVO);
        servico.getProdutos().add(produto);

        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.EM_DIAGNOSTICO);
        os.setServicos(new ArrayList<>(List.of(servico)));
        return os;
    }

    @Test
    @DisplayName("Deve remover produto, liberar estoque e recalcular total do serviço")
    void deveRemoverProdutoComSucesso() {
        UUID osId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();
        BigDecimal quantidade = new BigDecimal("3");

        OrdemDeServico os = criarOsComProduto(osId, produtoId, quantidade);
        when(osRepository.buscarPorId(osId)).thenReturn(Optional.of(os));

        OrdemDeServicoOutput resultado = useCase.executar(osId, produtoId);

        assertTrue(os.getServicos().get(0).getProdutos().isEmpty());
        verify(produtoGateway).liberarEstoqueReservado(produtoId, quantidade);
        verify(osRepository).atualizar(os);
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("Deve lançar exceção quando OS não for encontrada")
    void deveLancarExcecaoQuandoOsNaoEncontrada() {
        UUID osId = UUID.randomUUID();
        when(osRepository.buscarPorId(osId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> useCase.executar(osId, UUID.randomUUID()));
        verify(osRepository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando OS estiver encerrada")
    void deveLancarExcecaoQuandoOsEncerrada() {
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.ENTREGUE);

        when(osRepository.buscarPorId(osId)).thenReturn(Optional.of(os));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.executar(osId, UUID.randomUUID()));
        assertTrue(ex.getMessage().contains("encerrada"));
        verify(osRepository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não for encontrado em nenhum serviço")
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = criarOsComProduto(osId, UUID.randomUUID(), BigDecimal.ONE);

        when(osRepository.buscarPorId(osId)).thenReturn(Optional.of(os));

        UUID produtoInexistente = UUID.randomUUID();
        assertThrows(NotFoundException.class,
                () -> useCase.executar(osId, produtoInexistente));
        verify(osRepository, never()).atualizar(any());
    }
}









