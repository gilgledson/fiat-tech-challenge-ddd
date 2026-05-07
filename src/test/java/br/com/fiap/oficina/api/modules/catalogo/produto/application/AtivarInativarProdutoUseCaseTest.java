package br.com.fiap.oficina.api.modules.catalogo.produto.application;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase.AtivarProdutoUseCaseImpl;
import br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase.InativarProdutoUseCaseImpl;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.UnidadeMedida;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Ativar/Inativar Produto - Testes Unitários")
class AtivarInativarProdutoUseCaseTest {

    @Mock
    private ProdutoRepository repository;

    private AtivarProdutoUseCaseImpl ativarUseCase;
    private InativarProdutoUseCaseImpl inativarUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ativarUseCase = new AtivarProdutoUseCaseImpl(repository);
        inativarUseCase = new InativarProdutoUseCaseImpl(repository);
    }

    // ---------- INATIVAR ----------

    @Test
    @DisplayName("Deve inativar produto ativo com sucesso")
    void deveInativarComSucesso() {
        UUID id = UUID.randomUUID();
        Produto produto = Produto.reconstituir(id, "Produto", "123",
                BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, UnidadeMedida.UN, Optional.empty());
        when(repository.buscarPorId(id)).thenReturn(Optional.of(produto));

        inativarUseCase.executar(id);

        assertTrue(produto.getDeletadoEm().isPresent());
        verify(repository, times(1)).editar(any(Produto.class));
    }

    @Test
    @DisplayName("Inativar deve lançar NotFoundException quando produto não existir")
    void inativarDeveLancarNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> inativarUseCase.executar(id));
        verify(repository, never()).editar(any());
    }

    @Test
    @DisplayName("Inativar produto já inativo deve lançar IllegalArgumentException")
    void inativarProdutoJaInativoDeveLancarExcecao() {
        UUID id = UUID.randomUUID();
        Produto produto = Produto.reconstituir(id, "Produto", "123",
                BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, UnidadeMedida.UN, Optional.of(LocalDateTime.now()));
        when(repository.buscarPorId(id)).thenReturn(Optional.of(produto));

        assertThrows(IllegalArgumentException.class, () -> inativarUseCase.executar(id));
    }

    // ---------- ATIVAR ----------

    @Test
    @DisplayName("Deve ativar produto inativo com sucesso")
    void deveAtivarComSucesso() {
        UUID id = UUID.randomUUID();
        Produto produto = Produto.reconstituir(id, "Produto", "123",
                BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, UnidadeMedida.UN, Optional.of(LocalDateTime.now()));
        when(repository.buscarPorId(id)).thenReturn(Optional.of(produto));

        ativarUseCase.executar(id);

        assertTrue(produto.getDeletadoEm().isEmpty());
        verify(repository, times(1)).editar(any(Produto.class));
    }

    @Test
    @DisplayName("Ativar deve lançar NotFoundException quando produto não existir")
    void ativarDeveLancarNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> ativarUseCase.executar(id));
    }

    @Test
    @DisplayName("Ativar produto já ativo deve lançar IllegalArgumentException")
    void ativarProdutoJaAtivoDeveLancarExcecao() {
        UUID id = UUID.randomUUID();
        Produto produto = Produto.reconstituir(id, "Produto", "123",
                BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, UnidadeMedida.UN, Optional.empty());
        when(repository.buscarPorId(id)).thenReturn(Optional.of(produto));

        assertThrows(IllegalArgumentException.class, () -> ativarUseCase.executar(id));
    }
}









