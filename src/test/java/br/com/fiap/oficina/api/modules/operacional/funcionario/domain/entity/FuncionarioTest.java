package br.com.fiap.oficina.api.modules.operacional.funcionario.domain.entity;

import br.com.fiap.oficina.api.modules.operacional.funcionario.domain.valueObject.CargoFuncionario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Funcionário - Testes de Domínio")
class FuncionarioTest {

    @Test
    @DisplayName("Deve criar um funcionário com sucesso")
    void deveCriarFuncionario() {
        UUID usuarioId = UUID.randomUUID();
        Funcionario funcionario = new Funcionario(usuarioId, "João", "Silva", "123.456.789-00", "(11) 99999-9999", CargoFuncionario.MECANICO);

        assertNotNull(funcionario.getId());
        assertEquals(usuarioId, funcionario.getUsuarioId());
        assertEquals("João", funcionario.getNome());
        assertEquals("Silva", funcionario.getSobrenome());
        assertEquals("123.456.789-00", funcionario.getCpf());
        assertEquals("(11) 99999-9999", funcionario.getTelefone());
        assertEquals(CargoFuncionario.MECANICO, funcionario.getCargo());
        assertTrue(funcionario.isAtivo());
    }

    @Test
    @DisplayName("Deve inativar um funcionário")
    void deveInativarFuncionario() {
        Funcionario funcionario = new Funcionario(UUID.randomUUID(), "João", "Silva", "123.456.789-00", "(11) 99999-9999", CargoFuncionario.MECANICO);
        
        funcionario.desativar();
        
        assertFalse(funcionario.isAtivo());
        assertNotNull(funcionario.getDeletadoEm());
    }

    @Test
    @DisplayName("Deve ativar um funcionário inativo")
    void deveAtivarFuncionario() {
        Funcionario funcionario = new Funcionario(UUID.randomUUID(), "João", "Silva", "123.456.789-00", "(11) 99999-9999", CargoFuncionario.MECANICO);
        funcionario.desativar();
        
        funcionario.ativar();
        
        assertTrue(funcionario.isAtivo());
        assertNull(funcionario.getDeletadoEm());
    }

    @Test
    @DisplayName("Deve falhar ao ativar funcionário já ativo")
    void deveFalharAoAtivarJaAtivo() {
        Funcionario funcionario = new Funcionario(UUID.randomUUID(), "João", "Silva", "123.456.789-00", "(11) 99999-9999", CargoFuncionario.MECANICO);
        
        assertThrows(IllegalArgumentException.class, funcionario::ativar);
    }
}
