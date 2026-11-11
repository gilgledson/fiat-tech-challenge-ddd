package br.com.fiap.oficina.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class OrdemDeServicoSteps {

    /** PNG de 1x1 pixel, usado como assinatura do cliente no aceite do orçamento. */
    private static final String ASSINATURA =
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==";

    private final Api api = new Api();

    private String clienteId;
    private String veiculoId;
    private int estoqueInicial;
    private String produtoId;
    private String servicoId;
    private int quantidadePorServico;
    private String ordemId;
    private String servicoDaOrdemId;
    private String orcamentoId;

    // ---------- Contexto ----------

    @Dado("um atendente autenticado")
    public void umAtendenteAutenticado() {
        api.autenticar("admin@oficina.com.br", "admin123");
    }

    @Dado("um cliente com um veículo cadastrado")
    public void umClienteComUmVeiculoCadastrado() {
        String sufixo = sufixo();
        String usuarioId = criar("/api/usuarios", Map.of(
                "email", "cliente." + sufixo + "@oficina.com",
                "senha", "senha123",
                "perfil", "ADMIN"));
        clienteId = criar("/api/clientes", Map.of(
                "nome", "Cliente BDD " + sufixo,
                "email", "cliente.bdd." + sufixo + "@email.com",
                "cpf_cnpj", digitos(11),
                "telefone", "11999999999",
                "usuario_id", usuarioId,
                "endereco", Map.of(
                        "logradouro", "Rua das Oficinas",
                        "numero", "100",
                        "bairro", "Centro",
                        "cidade", "São Paulo",
                        "estado", "SP",
                        "cep", "01234567")));
        veiculoId = criar("/api/veiculos", Map.of(
                "cliente_id", clienteId,
                "placa", "BDD" + digitos(4),
                "marca", "Fiat",
                "modelo", "Uno",
                "ano", 2020));
    }

    @Dado("uma peça com {int} unidades em estoque")
    public void umaPecaComUnidadesEmEstoque(int quantidade) {
        // A peça é cadastrada no passo do serviço, que é quem informa o preço dela.
        estoqueInicial = quantidade;
    }

    @Dado("um serviço preventivo de {bigdecimal} que consome {int} unidade(s) da peça de {bigdecimal}")
    public void umServicoPreventivoQueConsomeAPeca(BigDecimal precoServico, int quantidade, BigDecimal precoPeca) {
        String sufixo = sufixo();
        produtoId = criar("/api/produtos", Map.of(
                "nome", "Óleo 5W30 BDD " + sufixo,
                "codigo_barras", "BDD-" + sufixo,
                "preco_unitario", precoPeca,
                "quantidade_estoque_fisico", estoqueInicial,
                "unidade_medida", "UN"));
        servicoId = criar("/api/servicos", Map.of(
                "nome", "Troca de Óleo BDD " + sufixo,
                "tipo", "PREVENTIVO",
                "preco_base", precoServico));
        quantidadePorServico = quantidade;
    }

    // ---------- OS Service ----------

    @Dado("uma ordem de serviço aberta com o serviço")
    public void umaOrdemDeServicoAbertaComOServico() {
        Response resposta = api.post("/api/ordens", Map.of(
                "cliente_id", clienteId,
                "veiculo_id", veiculoId,
                "descricao_problema", "Troca de óleo de rotina (BDD)",
                "servicos", List.of(Map.of(
                        "servico_id", servicoId,
                        "quantidade", 1,
                        "produtos", List.of(Map.of(
                                "produto_id", produtoId,
                                "quantidade", quantidadePorServico))))));
        assertSucesso(resposta);
        ordemId = resposta.jsonPath().getString("id");
        servicoDaOrdemId = resposta.jsonPath().getString("servicos[0].id");
        assertThat(servicoDaOrdemId).as("serviço incluído na OS").isNotBlank();
    }

    @Dado("que a peça tem {int} unidade(s) reservada(s)")
    public void queAPecaTemUnidadesReservadas(int reservadas) {
        Response produto = api.get("/api/produtos/" + produtoId);
        assertSucesso(produto);
        assertThat(produto.jsonPath().getDouble("quantidade_estoque_reservado")).isEqualTo(reservadas);
    }

    @Então("a ordem de serviço fica com o status {string}")
    public void aOrdemDeServicoFicaComOStatus(String status) {
        api.aguardar("OS " + ordemId + " com status " + status,
                () -> api.get("/api/ordens/" + ordemId),
                r -> r.statusCode() == 200 && status.equals(r.jsonPath().getString("status")));
    }

    @Então("a peça fica com {int} unidades em estoque e {int} reservadas")
    public void aPecaFicaComUnidadesEmEstoqueEReservadas(int fisico, int reservado) {
        api.aguardar("peça com " + fisico + " em estoque e " + reservado + " reservadas",
                () -> api.get("/api/produtos/" + produtoId),
                r -> r.statusCode() == 200
                        && r.jsonPath().getDouble("quantidade_estoque_fisico") == fisico
                        && r.jsonPath().getDouble("quantidade_estoque_reservado") == reservado);
    }

    // ---------- Execution Service ----------

    @Quando("o mecânico inicia o diagnóstico")
    public void oMecanicoIniciaODiagnostico() {
        // A execução só existe no Execution Service depois que o evento os.aberta chega lá.
        Response resposta = api.aguardar("execução criada a partir de os.aberta",
                () -> api.post("/api/execucoes/" + ordemId + "/iniciar-diagnostico"),
                r -> r.statusCode() != 404);
        assertSucesso(resposta);
    }

    @Quando("o mecânico conclui o diagnóstico")
    public void oMecanicoConcluiODiagnostico() {
        Response resposta = api.post("/api/execucoes/" + ordemId + "/concluir-diagnostico");
        assertSucesso(resposta);
        assertThat(resposta.jsonPath().getString("fase")).isEqualTo("AGUARDANDO_APROVACAO");
    }

    @Dado("que o diagnóstico foi concluído e o orçamento foi gerado")
    public void queODiagnosticoFoiConcluidoEOOrcamentoFoiGerado() {
        oMecanicoIniciaODiagnostico();
        oMecanicoConcluiODiagnostico();
        aguardarOrcamento();
    }

    @Quando("o mecânico executa o serviço e conclui a execução")
    public void oMecanicoExecutaOServicoEConcluiAExecucao() {
        // A fila de execução só é liberada depois que o evento os.aprovada chega ao Execution Service.
        Response inicio = api.aguardar("execução liberada a partir de os.aprovada",
                () -> api.post("/api/execucoes/" + ordemId + "/iniciar-execucao"),
                r -> r.statusCode() != 409 && r.statusCode() != 404);
        assertSucesso(inicio);

        String servico = "/api/execucoes/" + ordemId + "/servicos/" + servicoDaOrdemId;
        assertSucesso(api.post(servico + "/iniciar"));
        assertSucesso(api.post(servico + "/finalizar"));

        // Espera o OS Service refletir o serviço finalizado antes de encerrar a execução.
        api.aguardar("OS com o serviço finalizado",
                () -> api.get("/api/ordens/" + ordemId),
                r -> r.statusCode() == 200
                        && "FINALIZADO".equals(r.jsonPath().getString("servicos[0].status")));

        Response conclusao = api.post("/api/execucoes/" + ordemId + "/concluir-execucao");
        assertSucesso(conclusao);
        assertThat(conclusao.jsonPath().getString("fase")).isEqualTo("CONCLUIDA");
    }

    @Quando("a oficina reporta a falha {string}")
    public void aOficinaReportaAFalha(String motivo) {
        Response resposta = api.post("/api/execucoes/" + ordemId + "/falha", Map.of("motivo", motivo));
        assertSucesso(resposta);
    }

    @Então("a execução fica na fase {string}")
    public void aExecucaoFicaNaFase(String fase) {
        api.aguardar("execução na fase " + fase,
                () -> api.get("/api/execucoes/" + ordemId),
                r -> r.statusCode() == 200 && fase.equals(r.jsonPath().getString("fase")));
    }

    @Então("a execução fica na fase {string} com o motivo {string}")
    public void aExecucaoFicaNaFaseComOMotivo(String fase, String motivo) {
        aExecucaoFicaNaFase(fase);
        Response execucao = api.get("/api/execucoes/" + ordemId);
        assertThat(execucao.jsonPath().getString("motivoEncerramento")).contains(motivo);
    }

    // ---------- Billing Service ----------

    @Então("o Billing gera um orçamento de {bigdecimal} para a ordem de serviço")
    public void oBillingGeraUmOrcamentoParaAOrdemDeServico(BigDecimal valor) {
        Response orcamento = aguardarOrcamento();
        assertThat(new BigDecimal(orcamento.jsonPath().getString("valorTotal"))).isEqualByComparingTo(valor);
    }

    @Quando("o cliente aceita o orçamento")
    public void oClienteAceitaOOrcamento() {
        responderOrcamento(List.of(servicoDaOrdemId), List.of());
    }

    @Quando("o cliente rejeita o orçamento")
    public void oClienteRejeitaOOrcamento() {
        responderOrcamento(List.of(), List.of(servicoDaOrdemId));
    }

    @Quando("o pagamento do orçamento é confirmado")
    public void oPagamentoDoOrcamentoEConfirmado() {
        assertSucesso(api.post("/api/orcamentos/" + orcamentoId + "/confirmar-pagamento",
                Map.of("metodoPagamento", "PIX")));
    }

    @Então("o orçamento fica com o status {string}")
    public void oOrcamentoFicaComOStatus(String status) {
        api.aguardar("orçamento com status " + status,
                () -> api.get("/api/orcamentos/ordem-servico/" + ordemId + "/json"),
                r -> r.statusCode() == 200 && status.equals(r.jsonPath().getString("status")));
    }

    // ---------- Apoio ----------

    private Response aguardarOrcamento() {
        // O orçamento é gerado quando o evento os.diagnostico-concluido chega ao Billing.
        Response orcamento = api.aguardar("orçamento gerado a partir de os.diagnostico-concluido",
                () -> api.get("/api/orcamentos/ordem-servico/" + ordemId + "/json"),
                r -> r.statusCode() == 200);
        orcamentoId = orcamento.jsonPath().getString("id");
        return orcamento;
    }

    private void responderOrcamento(List<String> aceitos, List<String> rejeitados) {
        assertSucesso(api.post("/api/orcamentos/" + orcamentoId + "/aceitar", Map.of(
                "assinaturaBase64", ASSINATURA,
                "servicosAceitos", aceitos,
                "servicosRejeitados", rejeitados)));
    }

    private String criar(String caminho, Map<String, Object> corpo) {
        Response resposta = api.post(caminho, corpo);
        assertSucesso(resposta);
        return resposta.jsonPath().getString("id");
    }

    private static void assertSucesso(Response resposta) {
        assertThat(resposta.statusCode())
                .as("resposta: %s", resposta.asString())
                .isBetween(200, 299);
    }

    private static String sufixo() {
        return Long.toString(System.nanoTime(), 36);
    }

    private static String digitos(int quantidade) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < quantidade; i++) {
            sb.append(ThreadLocalRandom.current().nextInt(10));
        }
        return sb.toString();
    }
}
