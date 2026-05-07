package br.com.fiap.oficina.api.modules.atendimento.veiculo.domain;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.domain.entity.Veiculo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Veiculo - Testes de Domínio")
class VeiculoTest {

    private UUID clienteId = UUID.randomUUID();

    private Veiculo veiculoValido() {
        return new Veiculo(clienteId, "ABC-1234", "Toyota", "Corolla", 2022);
    }

    @Test
    @DisplayName("Deve criar veículo válido com sucesso")
    void deveCriarVeiculoValido() {
        Veiculo v = veiculoValido();
        assertNotNull(v.getId());
        assertEquals("ABC-1234", v.getPlaca());
        assertTrue(v.getDeletadoEm().isEmpty());
    }

    @Test
    @DisplayName("Não deve criar veículo com placa em branco")
    void naoDeveCriarComPlacaEmBranco() {
        assertThrows(IllegalArgumentException.class, () ->
            new Veiculo(clienteId, "", "Toyota", "Corolla", 2022)
        );
    }

    @Test
    @DisplayName("Não deve criar veículo com marca em branco")
    void naoDeveCriarComMarcaEmBranco() {
        assertThrows(IllegalArgumentException.class, () ->
            new Veiculo(clienteId, "ABC-1234", "", "Corolla", 2022)
        );
    }

    @Test
    @DisplayName("Não deve criar veículo com ano inválido (muito antigo)")
    void naoDeveCriarComAnoInvalido() {
        assertThrows(IllegalArgumentException.class, () ->
            new Veiculo(clienteId, "ABC-1234", "Toyota", "Corolla", 1800)
        );
    }

    @Test
    @DisplayName("Não deve criar veículo sem clienteId")
    void naoDeveCriarSemClienteId() {
        assertThrows(IllegalArgumentException.class, () ->
            new Veiculo(null, "ABC-1234", "Toyota", "Corolla", 2022)
        );
    }

    @Test
    @DisplayName("inativar deve marcar deletadoEm")
    void deveInativarVeiculo() {
        Veiculo v = veiculoValido();
        v.inativar();
        assertTrue(v.getDeletadoEm().isPresent());
    }

    @Test
    @DisplayName("inativar veículo já inativo deve lançar exceção")
    void naoDeveInativarVeiculoJaInativo() {
        Veiculo v = Veiculo.reconstituir(UUID.randomUUID(), clienteId, "ABC-1234", "Toyota", "Corolla", 2022,
                Optional.of(LocalDateTime.now()));
        assertThrows(IllegalArgumentException.class, v::inativar);
    }

    @Test
    @DisplayName("ativar deve limpar deletadoEm")
    void deveAtivarVeiculo() {
        Veiculo v = Veiculo.reconstituir(UUID.randomUUID(), clienteId, "ABC-1234", "Toyota", "Corolla", 2022,
                Optional.of(LocalDateTime.now()));
        v.ativar();
        assertTrue(v.getDeletadoEm().isEmpty());
    }

    @Test
    @DisplayName("ativar veículo já ativo deve lançar exceção")
    void naoDeveAtivarVeiculoJaAtivo() {
        Veiculo v = veiculoValido();
        assertThrows(IllegalArgumentException.class, v::ativar);
    }
}









