package br.com.fiap.oficina.bdd;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.time.Duration;
import java.util.function.Predicate;
import org.awaitility.Awaitility;

/** Cliente HTTP do gateway que fica na frente dos três microsserviços. */
final class Api {

    private static final String BASE_URL = System.getProperty("baseUrl", "http://localhost:8080");
    private static final Duration ESPERA_MAXIMA = Duration.ofSeconds(30);
    private static final Duration INTERVALO = Duration.ofMillis(500);

    private String token;

    void autenticar(String email, String senha) {
        Response resposta = post("/api/usuarios/login", new Login(email, senha));
        resposta.then().statusCode(200);
        token = resposta.jsonPath().getString("access_token");
    }

    Response get(String caminho) {
        return requisicao().get(caminho);
    }

    Response post(String caminho) {
        // Sem corpo, mas com o Content-Type que os endpoints declaram consumir.
        return requisicao().contentType(ContentType.JSON).post(caminho);
    }

    Response post(String caminho, Object corpo) {
        return requisicao().contentType(ContentType.JSON).body(corpo).post(caminho);
    }

    /**
     * Os passos da Saga chegam ao serviço vizinho por evento, então o efeito de um comando
     * demora alguns instantes. Repete a chamada até a resposta ser a esperada.
     */
    Response aguardar(String descricao, java.util.function.Supplier<Response> chamada, Predicate<Response> pronto) {
        Response[] ultima = new Response[1];
        Awaitility.await(descricao)
                .atMost(ESPERA_MAXIMA)
                .pollInterval(INTERVALO)
                .until(() -> {
                    ultima[0] = chamada.get();
                    return pronto.test(ultima[0]);
                });
        return ultima[0];
    }

    private RequestSpecification requisicao() {
        RequestSpecification spec = given().baseUri(BASE_URL).accept(ContentType.JSON);
        return token == null ? spec : spec.header("Authorization", "Bearer " + token);
    }

    private record Login(String email, String senha) {
    }
}
