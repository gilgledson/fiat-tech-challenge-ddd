package br.com.fiap.oficina.api.modules.identidade.domain.entity;

import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    @DisplayName("Construtor simples deve gerar UUID e definir ativo=true")
    void deveCriarUsuarioComConstrutorSimples() {
        Usuario usuario = new Usuario("email@test.com", "hash123", PerfilUsuario.CLIENTE);

        assertNotNull(usuario.getId());
        assertEquals("email@test.com", usuario.getEmail());
        assertEquals("hash123", usuario.getSenhaHash());
        assertEquals(PerfilUsuario.CLIENTE, usuario.getPerfil());
        assertTrue(usuario.isAtivo());
    }

    @Test
    @DisplayName("Construtor completo deve preservar todos os campos incluindo ativo=false")
    void deveCriarUsuarioComConstrutorCompleto() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario(id, "admin@test.com", "hashadmin", PerfilUsuario.ADMIN, false);

        assertEquals(id, usuario.getId());
        assertEquals("admin@test.com", usuario.getEmail());
        assertEquals("hashadmin", usuario.getSenhaHash());
        assertEquals(PerfilUsuario.ADMIN, usuario.getPerfil());
        assertFalse(usuario.isAtivo());
    }

    @Test
    @DisplayName("Deve permitir alterar o campo ativo via setter")
    void deveAlterarAtivo() {
        Usuario usuario = new Usuario("mecanico@test.com", "hash", PerfilUsuario.MECANICO);
        assertTrue(usuario.isAtivo());

        usuario.setAtivo(false);
        assertFalse(usuario.isAtivo());

        usuario.setAtivo(true);
        assertTrue(usuario.isAtivo());
    }

    @Test
    @DisplayName("Dois usuários criados com construtor simples devem ter IDs diferentes")
    void deveTerIdsUnicosParaCadaInstancia() {
        Usuario u1 = new Usuario("a@a.com", "h1", PerfilUsuario.ATENDENTE);
        Usuario u2 = new Usuario("b@b.com", "h2", PerfilUsuario.ATENDENTE);

        assertNotEquals(u1.getId(), u2.getId());
    }

    @Test
    @DisplayName("PerfilUsuario deve converter de string corretamente")
    void deveConverterPerfilDeString() {
        assertEquals(PerfilUsuario.ADMIN, PerfilUsuario.fromString("ADMIN"));
        assertEquals(PerfilUsuario.MECANICO, PerfilUsuario.fromString("mecanico"));
        assertEquals(PerfilUsuario.CLIENTE, PerfilUsuario.fromString("cliente"));
    }
}









