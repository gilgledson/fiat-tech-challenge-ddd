package br.com.fiap.oficina.api.modules.faturamento.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.faturamento.domain.valueObject.MetodoPagamento;
import br.com.fiap.oficina.api.modules.faturamento.domain.valueObject.StatusFatura;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Fatura {
    private UUID id;
    private LocalDate dataEmissao;
    private UUID ordemServicoId;
    private LocalDate dataVencimento;
    private MetodoPagamento metodoPagamento;
    private BigDecimal valorTotal;
    private StatusFatura status;

    public static Fatura reconstruir(UUID id, LocalDate dataEmissao, UUID ordemServicoId, LocalDate dataVencimento,
            BigDecimal valorTotal, StatusFatura status, MetodoPagamento metodoPagamento) {
        return new Fatura(id, dataEmissao, ordemServicoId, dataVencimento, valorTotal, status, metodoPagamento);
    }

    public Fatura(UUID id, LocalDate dataEmissao, UUID ordemServicoId, LocalDate dataVencimento,
            BigDecimal valorTotal, StatusFatura status, MetodoPagamento metodoPagamento) {
        this.id = id;
        this.dataEmissao = dataEmissao;
        this.ordemServicoId = ordemServicoId;
        this.dataVencimento = dataVencimento;
        this.valorTotal = valorTotal;
        this.status = status;
        this.metodoPagamento = metodoPagamento;
    }

    public Fatura(UUID ordemServicoId, BigDecimal valorTotal, MetodoPagamento metodoPagamento) {
        this.id = UUID.randomUUID();
        this.dataEmissao = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
        this.ordemServicoId = ordemServicoId;
        this.dataVencimento = this.dataEmissao.plusDays(30);
        this.valorTotal = valorTotal;
        this.status = StatusFatura.PENDENTE;
        this.metodoPagamento = metodoPagamento;
    }

    public void validar() {
        if (ordemServicoId == null) {
            throw new IllegalArgumentException("Ordem de serviço é obrigatória.");
        }
        if (dataEmissao == null) {
            throw new IllegalArgumentException("Data de emissão é obrigatória.");
        }
        if (dataVencimento == null) {
            throw new IllegalArgumentException("Data de vencimento é obrigatória.");
        }
        if (valorTotal == null) {
            throw new IllegalArgumentException("Valor total é obrigatório.");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status é obrigatório.");
        }
    }

    public void atualizar(UUID ordemServicoId, LocalDate dataEmissao, LocalDate dataVencimento, BigDecimal valorTotal,
            StatusFatura status, MetodoPagamento metodoPagamento) {
        this.ordemServicoId = ordemServicoId;
        this.dataEmissao = dataEmissao;
        this.dataVencimento = dataVencimento;
        this.valorTotal = valorTotal;
        this.status = status;
        this.metodoPagamento = metodoPagamento;
    }

    public void cancelar() {
        this.status = StatusFatura.CANCELADA;
    }

    public void pagar(MetodoPagamento metodoPagamento) {
        if (metodoPagamento == null) {
            throw new IllegalArgumentException("Método de pagamento é obrigatório para confirmar o pagamento.");
        }
        this.status = StatusFatura.PAGO;
        this.metodoPagamento = metodoPagamento;
    }

    public void pendente() {
        this.status = StatusFatura.PENDENTE;
    }
}
