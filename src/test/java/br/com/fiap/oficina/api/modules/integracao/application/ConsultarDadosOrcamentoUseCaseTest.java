package br.com.fiap.oficina.api.modules.integracao.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.entity.Cliente;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.valueobject.Endereco;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.domain.entity.Veiculo;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoProdutos;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicos;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;

@ExtendWith(MockitoExtension.class)
class ConsultarDadosOrcamentoUseCaseTest {

    @Mock
    private OrdemDeServicoRepository ordemDeServicoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private ConsultarDadosOrcamentoUseCase useCase;

    private static OrdemDeServicoServicos servico(String nome, OrdemDeServicoServicoStatus status) {
        return new OrdemDeServicoServicos(UUID.randomUUID(), UUID.randomUUID(), nome, 1, new BigDecimal("100.00"),
                new BigDecimal("100.00"), status, TipoServico.PREVENTIVO);
    }

    @Test
    @DisplayName("OS: devolve só os serviços aprovados pelo cliente, com seus produtos")
    void deveBuscarOrdemDeServicoComServicosAprovados() {
        UUID osId = UUID.randomUUID();
        UUID clienteId = UUID.randomUUID();
        UUID veiculoId = UUID.randomUUID();
        OrdemDeServicoProdutos produto = mock(OrdemDeServicoProdutos.class);
        when(produto.getNomeDoProduto()).thenReturn("Óleo 5W30");
        when(produto.getQuantidade()).thenReturn(new BigDecimal("4"));
        when(produto.getPrecoUnitario()).thenReturn(new BigDecimal("35.00"));
        when(produto.getTotal()).thenReturn(new BigDecimal("140.00"));
        OrdemDeServicoServicos aprovado = servico("Troca de óleo", OrdemDeServicoServicoStatus.APROVADO);
        aprovado.setProdutos(new ArrayList<>(List.of(produto)));
        OrdemDeServicoServicos finalizado = servico("Alinhamento", OrdemDeServicoServicoStatus.FINALIZADO);
        OrdemDeServicoServicos cancelado = servico("Polimento", OrdemDeServicoServicoStatus.CANCELADO);
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setClienteId(clienteId);
        os.setVeiculoId(veiculoId);
        os.setServicos(new ArrayList<>(List.of(aprovado, finalizado, cancelado)));
        when(ordemDeServicoRepository.buscarPorId(osId)).thenReturn(Optional.of(os));

        DadosOrcamento.OrdemServico dados = useCase.buscarOrdemDeServico(osId).orElseThrow();

        assertEquals(osId, dados.id());
        assertEquals(clienteId, dados.clienteId());
        assertEquals(veiculoId, dados.veiculoId());
        assertEquals(os.calcularValorTotal(), dados.valorTotal());
        assertEquals(List.of("Troca de óleo", "Alinhamento"),
                dados.servicos().stream().map(DadosOrcamento.ItemServico::nome).toList());
        DadosOrcamento.ItemServico item = dados.servicos().get(0);
        assertEquals(1, item.quantidade());
        assertEquals(new BigDecimal("100.00"), item.precoUnitario());
        assertEquals(List.of(new DadosOrcamento.ItemProduto("Óleo 5W30", new BigDecimal("4"),
                new BigDecimal("35.00"), new BigDecimal("140.00"))), item.produtos());
        assertTrue(dados.servicos().get(1).produtos().isEmpty());
    }

    @Test
    @DisplayName("Cliente: monta o endereço completo, com e sem complemento")
    void deveBuscarClienteComEnderecoCompleto() {
        UUID id = UUID.randomUUID();
        Endereco endereco = mock(Endereco.class);
        when(endereco.getLogradouro()).thenReturn("Rua das Flores");
        when(endereco.getNumero()).thenReturn("100");
        when(endereco.getComplemento()).thenReturn("Apto 12");
        when(endereco.getBairro()).thenReturn("Centro");
        when(endereco.getCidade()).thenReturn("São Paulo");
        when(endereco.getEstado()).thenReturn("SP");
        when(endereco.getCep()).thenReturn("01000-000");
        Cliente cliente = mock(Cliente.class);
        when(cliente.getId()).thenReturn(id);
        when(cliente.getNome()).thenReturn("Maria");
        when(cliente.getEmail()).thenReturn("maria@exemplo.com");
        when(cliente.getCpfCnpj()).thenReturn("123.456.789-09");
        when(cliente.getTelefone()).thenReturn("(11) 99999-0000");
        when(cliente.getEndereco()).thenReturn(endereco);
        when(clienteRepository.buscarPorId(id)).thenReturn(Optional.of(cliente));

        DadosOrcamento.Cliente comComplemento = useCase.buscarCliente(id).orElseThrow();
        when(endereco.getComplemento()).thenReturn(null);
        DadosOrcamento.Cliente semComplemento = useCase.buscarCliente(id).orElseThrow();

        assertEquals(new DadosOrcamento.Cliente(id, "Maria", "maria@exemplo.com", "123.456.789-09",
                "(11) 99999-0000", "Rua das Flores, 100 (Apto 12) - Centro, São Paulo - SP, CEP: 01000-000"),
                comComplemento);
        assertEquals("Rua das Flores, 100 - Centro, São Paulo - SP, CEP: 01000-000",
                semComplemento.enderecoCompleto());
    }

    @Test
    @DisplayName("Veículo: devolve os dados de identificação")
    void deveBuscarVeiculo() {
        UUID id = UUID.randomUUID();
        Veiculo veiculo = mock(Veiculo.class);
        when(veiculo.getId()).thenReturn(id);
        when(veiculo.getPlaca()).thenReturn("ABC1D23");
        when(veiculo.getMarca()).thenReturn("Fiat");
        when(veiculo.getModelo()).thenReturn("Argo");
        when(veiculo.getAno()).thenReturn(2022);
        when(veiculoRepository.buscarPorId(id)).thenReturn(Optional.of(veiculo));

        assertEquals(Optional.of(new DadosOrcamento.Veiculo(id, "ABC1D23", "Fiat", "Argo", 2022)),
                useCase.buscarVeiculo(id));
    }

    @Test
    @DisplayName("Registros inexistentes devolvem vazio")
    void deveDevolverVazioQuandoNaoEncontrado() {
        UUID id = UUID.randomUUID();
        when(ordemDeServicoRepository.buscarPorId(id)).thenReturn(Optional.empty());
        when(clienteRepository.buscarPorId(id)).thenReturn(Optional.empty());
        when(veiculoRepository.buscarPorId(id)).thenReturn(Optional.empty());

        assertTrue(useCase.buscarOrdemDeServico(id).isEmpty());
        assertTrue(useCase.buscarCliente(id).isEmpty());
        assertTrue(useCase.buscarVeiculo(id).isEmpty());
    }
}
