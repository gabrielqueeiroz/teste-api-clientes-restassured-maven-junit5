
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import java.util.ArrayList;
import java.util.List;

public class Restricoes {
    private static final String URL_RESTRICOES = "http://localhost:8080/api/v1/restricoes/";

    @Test
    @DisplayName("Quando fornecida lista de CPF restritos Então deve ser exibida a mensagem: \"O CPF\n 'x' possui restrição\"")
    public void consultaRestricao(){
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente("97093236014"));
        clientes.add(new Cliente("26276298085"));
        clientes.add(new Cliente("01317496094"));
        clientes.add(new Cliente("55856777050"));
        clientes.add(new Cliente("19626829001"));
        clientes.add(new Cliente("24094592008"));
        clientes.add(new Cliente("58063164083"));
        clientes.add(new Cliente("62648716050"));
        clientes.add(new Cliente("84809766080"));
        clientes.add(new Cliente("60094146012"));

        String preRespostaEsperada = "O CPF ";
        String posRespostaEsperada = " tem problema";

        for (Cliente cliente: clientes) {
            given()
                    .contentType(ContentType.JSON)
            .when()
                .get(URL_RESTRICOES+cliente.getCpf())
            .then()
                .statusCode(200)
                .assertThat()
                .body("mensagem", equalTo(preRespostaEsperada + cliente.getCpf() + posRespostaEsperada));
        }
    }

    @Test
    @DisplayName("Quando fornecido CPF sem restrição Então deve ser retornado o HTTP Status 204 No Content")
    public void consultaNaoRestricao(){
        given()
        .when()
            .get(URL_RESTRICOES+"99999999999")
        .then()
            .assertThat()
            .statusCode(204);
    }
}
