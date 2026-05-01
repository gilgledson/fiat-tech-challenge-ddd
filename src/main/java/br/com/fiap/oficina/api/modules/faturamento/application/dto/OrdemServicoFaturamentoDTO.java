package br.com.fiap.oficina.api.modules.faturamento.application.dto;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class OrdemServicoFaturamentoDTO {
    public OrdemServicoFaturamentoDTO(UUID id, UUID clienteId, UUID veiculoId, List<ItemServicoDTO> servicos,
            List<ItemProdutoDTO> produtos, BigDecimal valorTotal) {
        this.id = id;
        this.clienteId = clienteId;
        this.veiculoId = veiculoId;
        this.servicos = servicos;
        this.produtos = produtos;
        this.valorTotal = valorTotal;
    }

    public UUID getId() { return id; }
    public UUID getClienteId() { return clienteId; }
    public UUID getVeiculoId() { return veiculoId; }
    public List<ItemServicoDTO> getServicos() { return servicos; }
    public List<ItemProdutoDTO> getProdutos() { return produtos; }
    public BigDecimal getValorTotal() { return valorTotal; }

    private UUID id;
    private UUID clienteId;
    private UUID veiculoId;
    private List<ItemServicoDTO> servicos;
    private List<ItemProdutoDTO> produtos;
    private BigDecimal valorTotal;

    public static class ItemServicoDTO {
        public ItemServicoDTO(String nome, int quantidade, BigDecimal precoUnitario, BigDecimal total) {
            this.nome = nome;
            this.quantidade = quantidade;
            this.precoUnitario = precoUnitario;
            this.total = total;
        }

        public String getNome() { return nome; }
        public int getQuantidade() { return quantidade; }
        public BigDecimal getPrecoUnitario() { return precoUnitario; }
        public BigDecimal getTotal() { return total; }

        private String nome;
        private int quantidade;
        private BigDecimal precoUnitario;
        private BigDecimal total;
    }

    public static class ItemProdutoDTO {
        public ItemProdutoDTO(String nome, BigDecimal quantidade, BigDecimal precoUnitario, BigDecimal total) {
            this.nome = nome;
            this.quantidade = quantidade;
            this.precoUnitario = precoUnitario;
            this.total = total;
        }

        public String getNome() { return nome; }
        public BigDecimal getQuantidade() { return quantidade; }
        public BigDecimal getPrecoUnitario() { return precoUnitario; }
        public BigDecimal getTotal() { return total; }

        private String nome;
        private BigDecimal quantidade;
        private BigDecimal precoUnitario;
        private BigDecimal total;
    }
}
