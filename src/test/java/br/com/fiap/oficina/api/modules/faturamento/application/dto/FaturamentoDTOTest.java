package br.com.fiap.oficina.api.modules.faturamento.application.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FaturamentoDTOTest {

    @Test
    @DisplayName("ClienteFaturamentoDTO deve armazenar todos os campos corretamente")
    void deveArmazenarClienteFaturamentoDTO() {
        UUID id = UUID.randomUUID();
        ClienteFaturamentoDTO dto = new ClienteFaturamentoDTO(
                id, "João Silva", "joao@test.com", "12345678901", "(11)99999-9999", "Rua A, 100, SP");

        assertEquals(id, dto.getId());
        assertEquals("João Silva", dto.getNome());
        assertEquals("joao@test.com", dto.getEmail());
        assertEquals("12345678901", dto.getCpfCnpj());
        assertEquals("(11)99999-9999", dto.getTelefone());
        assertEquals("Rua A, 100, SP", dto.getEnderecoCompleto());
    }

    @Test
    @DisplayName("VeiculoFaturamentoDTO deve armazenar todos os campos corretamente")
    void deveArmazenarVeiculoFaturamentoDTO() {
        UUID id = UUID.randomUUID();
        VeiculoFaturamentoDTO dto = new VeiculoFaturamentoDTO(id, "ABC-1234", "Honda", "Civic", 2022);

        assertEquals(id, dto.getId());
        assertEquals("ABC-1234", dto.getPlaca());
        assertEquals("Honda", dto.getMarca());
        assertEquals("Civic", dto.getModelo());
        assertEquals(2022, dto.getAno());
    }

    @Test
    @DisplayName("OrdemServicoFaturamentoDTO deve armazenar campos e inner classes")
    void deveArmazenarOrdemServicoFaturamentoDTO() {
        UUID id = UUID.randomUUID();
        UUID clienteId = UUID.randomUUID();
        UUID veiculoId = UUID.randomUUID();
        BigDecimal total = new BigDecimal("750.00");

        OrdemServicoFaturamentoDTO.ItemProdutoDTO produto = new OrdemServicoFaturamentoDTO.ItemProdutoDTO(
                "Óleo 5W30", new BigDecimal("2"), new BigDecimal("50.00"), new BigDecimal("100.00"));

        OrdemServicoFaturamentoDTO.ItemServicoDTO servico = new OrdemServicoFaturamentoDTO.ItemServicoDTO(
                "Troca de óleo", 1, new BigDecimal("150.00"), new BigDecimal("250.00"), List.of(produto));

        OrdemServicoFaturamentoDTO dto = new OrdemServicoFaturamentoDTO(
                id, clienteId, veiculoId, List.of(servico), total);

        assertEquals(id, dto.getId());
        assertEquals(clienteId, dto.getClienteId());
        assertEquals(veiculoId, dto.getVeiculoId());
        assertEquals(total, dto.getValorTotal());
        assertEquals(1, dto.getServicos().size());

        OrdemServicoFaturamentoDTO.ItemServicoDTO s = dto.getServicos().get(0);
        assertEquals("Troca de óleo", s.getNome());
        assertEquals(1, s.getQuantidade());
        assertEquals(new BigDecimal("150.00"), s.getPrecoUnitario());
        assertEquals(new BigDecimal("250.00"), s.getTotal());
        assertEquals(1, s.getProdutos().size());

        OrdemServicoFaturamentoDTO.ItemProdutoDTO p = s.getProdutos().get(0);
        assertEquals("Óleo 5W30", p.getNome());
        assertEquals(new BigDecimal("2"), p.getQuantidade());
        assertEquals(new BigDecimal("50.00"), p.getPrecoUnitario());
        assertEquals(new BigDecimal("100.00"), p.getTotal());
    }
}
