package br.com.fiap.oficina.api.modules.faturamento.application.usecase.consultas;

import br.com.fiap.oficina.api.modules.faturamento.application.dto.ClienteFaturamentoDTO;
import br.com.fiap.oficina.api.modules.faturamento.application.dto.OrdemServicoFaturamentoDTO;
import br.com.fiap.oficina.api.modules.faturamento.application.dto.VeiculoFaturamentoDTO;
import br.com.fiap.oficina.api.modules.faturamento.application.gateway.AtendimentoGateway;
import br.com.fiap.oficina.api.modules.faturamento.application.gateway.OperacionalGateway;
import br.com.fiap.oficina.api.modules.faturamento.application.repository.FaturaRepository;
import br.com.fiap.oficina.api.modules.faturamento.domain.entity.Fatura;
import io.quarkus.qute.Template;
import io.quarkus.qute.Location;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

@ApplicationScoped
public class BaixarFaturaUseCaseImpl implements BaixarFaturaUseCase {

        @Inject
        FaturaRepository repository;

        @Inject
        AtendimentoGateway atendimentoGateway;

        @Inject
        OperacionalGateway operacionalGateway;

        @Inject
        @Location("faturamento/fatura")
        Template templateFatura;

        @Override
        public byte[] executar(UUID id) {
                Fatura fatura = repository.buscarPorId(id)
                                .orElseThrow(() -> new RuntimeException("Fatura não encontrada"));

                OrdemServicoFaturamentoDTO os = operacionalGateway.buscarOrdemDeServicoPorId(fatura.getOrdemServicoId())
                                .orElseThrow(() -> new RuntimeException("Ordem de serviço não encontrada"));

                ClienteFaturamentoDTO cliente = atendimentoGateway.buscarClientePorId(os.getClienteId())
                                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

                VeiculoFaturamentoDTO veiculo = atendimentoGateway.buscarVeiculoPorId(os.getVeiculoId())
                                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

                String htmlRenderizado = templateFatura
                                .data("fatura", fatura)
                                .data("os", os)
                                .data("cliente", cliente)
                                .data("veiculo", veiculo)
                                .render();

                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        ITextRenderer renderer = new ITextRenderer();
                        renderer.setDocumentFromString(htmlRenderizado);
                        renderer.layout();
                        renderer.createPDF(outputStream);
                        return outputStream.toByteArray();
                } catch (Exception e) {
                        throw new RuntimeException("Erro ao gerar PDF da fatura", e);
                }
        }
}
