package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;

import java.math.BigDecimal;
import java.util.UUID;

public record ServicoOrcamentoRequest(

                @NotNull(message = "O ID do serviço é obrigatório") @Schema(description = "ID do serviço do catálogo", defaultValue = "f9e8d7c6-0000-0000-0000-000000000001") @JsonProperty("servico_id") UUID servicoId,

                @NotBlank(message = "O nome do serviço é obrigatório") @Schema(description = "Nome do serviço para registro na OS", defaultValue = "Troca de Óleo e Filtros") String nome,

                @Min(value = 1, message = "A quantidade deve ser maior que zero") @Schema(description = "Quantidade de vezes que o serviço será executado", defaultValue = "1") int quantidade,

                @NotNull(message = "O valor unitário é obrigatório") @DecimalMin(value = "0.01", message = "O valor unitário deve ser maior que zero") @Schema(description = "Valor cobrado por cada execução do serviço", defaultValue = "150.00") @JsonProperty("valor_unitario") BigDecimal valorUnitario,
                
                @NotNull(message = "O tipo de serviço é obrigatório") @Schema(description = "Tipo de serviço (CORRETIVO ou PREVENTIVO)", defaultValue = "PREVENTIVO") TipoServico tipo) {
        public BigDecimal calcularTotal() {
                return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
        }
}






