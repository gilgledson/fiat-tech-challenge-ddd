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
class RemoverServicoOrdemDeServicoUseCaseTest {

    @Mock
    private OrdemDeServicoRepository osRepository;

    @Mock
    private CatalogoProdutoGateway produtoGateway;

    @InjectMocks
    private RemoverServicoOrdemDeServicoUseCaseImpl useCase;

    @Test
    @DisplayName("Deve remover serviço e liberar estoque dos produtos associados")
    void deveRemoverServicoELiberarEstoque() {
        UUID osId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();

        OrdemDeServicoServicos servico = new OrdemDeServicoServicos(
                osId, UUID.randomUUID(), "Troca de pastilha", 1,
                BigDecimal.TEN, BigDecimal.TEN, OrdemDeServicoServicoStatus.PENDENTE, TipoServico.CORRETIVO);

        OrdemDeServicoProdutos produto = new OrdemDeServicoProdutos();
        produto.setProdutoId(produtoId);
        produto.setQuantidade(new BigDecimal("1"));
        servico.getProdutos().add(produto);

        UUID servicoId = servico.getId();

        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.EM_DIAGNOSTICO);
        os.setServicos(new ArrayList<>(List.of(servico)));

        when(osRepository.buscarPorId(osId)).thenReturn(Optional.of(os));

        OrdemDeServicoOutput resultado = useCase.executar(osId, servicoId);

        assertTrue(os.getServicos().isEmpty());
        verify(produtoGateway).liberarEstoqueReservado(produtoId, new BigDecimal("1"));
        verify(osRepository).atualizar(os);
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("Deve remover serviço sem produtos sem chamar gateway")
    void deveRemoverServicoSemProdutos() {
        UUID osId = UUID.randomUUID();
        OrdemDeServicoServicos servico = new OrdemDeServicoServicos(
                osId, UUID.randomUUID(), "Diagnóstico", 1,
                BigDecimal.TEN, BigDecimal.TEN, OrdemDeServicoServicoStatus.PENDENTE, TipoServico.PREVENTIVO);
        UUID servicoId = servico.getId();

        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.EM_DIAGNOSTICO);
        os.setServicos(new ArrayList<>(List.of(servico)));

        when(osRepository.buscarPorId(osId)).thenReturn(Optional.of(os));

        useCase.executar(osId, servicoId);

        verifyNoInteractions(produtoGateway);
        verify(osRepository).atualizar(os);
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
        os.setStatus(OrdemDeServicoStatus.CANCELADA);

        when(osRepository.buscarPorId(osId)).thenReturn(Optional.of(os));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.executar(osId, UUID.randomUUID()));
        assertTrue(ex.getMessage().contains("encerrada"));
        verify(osRepository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando serviço não for encontrado na OS")
    void deveLancarExcecaoQuandoServicoNaoEncontrado() {
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.EM_DIAGNOSTICO);
        os.setServicos(new ArrayList<>());

        when(osRepository.buscarPorId(osId)).thenReturn(Optional.of(os));

        assertThrows(NotFoundException.class,
                () -> useCase.executar(osId, UUID.randomUUID()));
        verify(osRepository, never()).atualizar(any());
    }
}









