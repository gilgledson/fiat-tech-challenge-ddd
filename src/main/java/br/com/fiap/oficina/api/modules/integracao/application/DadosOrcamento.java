package br.com.fiap.oficina.api.modules.integracao.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Formatos de resposta da API de integração consumida pelo Billing Service
 * para montar o PDF do orçamento. Fazem parte do contrato entre os serviços.
 */
public final class DadosOrcamento {

    private DadosOrcamento() {
    }

    public record OrdemServico(UUID id, UUID clienteId, UUID veiculoId, List<ItemServico> servicos,
            BigDecimal valorTotal) {
    }

    public record ItemServico(String nome, int quantidade, BigDecimal precoUnitario, BigDecimal total,
            List<ItemProduto> produtos) {
    }

    public record ItemProduto(String nome, BigDecimal quantidade, BigDecimal precoUnitario, BigDecimal total) {
    }

    public record Cliente(UUID id, String nome, String email, String cpfCnpj, String telefone,
            String enderecoCompleto) {
    }

    public record Veiculo(UUID id, String placa, String marca, String modelo, int ano) {
    }
}
