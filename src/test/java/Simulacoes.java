import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class Simulacoes {
    private static final String URL_SIMULACOES = "http://localhost:8080/api/v1/simulacoes/";

    @Test
    @DisplayName("Quando eu informar dados novos válidos, Então os dados devem ser registrados")
    public void quandoInformarDadosValidos_DadosSaoRegistrados() {
        Cliente cliente = new Cliente("12345678910", "Teste Sicredi", "email@email.com", "1200", "3", "true");

        postaCliente(cliente)
            .statusCode(HttpStatus.SC_CREATED)
            .body("nome", equalTo("Teste Sicredi"))
            .body("cpf", equalTo("12345678910"))
            .body("email", equalTo("email@email.com"))
            .body("valor", equalTo(1200))
            .body("parcelas", equalTo(3))
            .body("seguro", equalTo(true));
    }

    @Test
    @DisplayName("Quando eu informar dados com algum problema, Então deve ser apresentada uma lista de erros")
    public void quandoInformarDadosErrados_GeraListaDeErros() {
        Cliente cliente = new Cliente("12345678910", "Teste Sicredi", "email@email.com", "1200", "3", "true");

        postaCliente(cliente)
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Quando eu informar um CPF repetido, Então deve ser exibida a mensagem: \"CPF já existente\"")
    public void quandoCpfRepetido_GeraMensagem() {
        Cliente cliente = new Cliente("12345678910", "Teste Sicredi", "email@email.com", "1200", "3", "true");

        apagaCliente(cliente);
        postaCliente(cliente);
        postaCliente(cliente)
            .statusCode(HttpStatus.SC_CONFLICT)
                .body("mensagem", equalTo("CPF duplicado"));
    }

    @Test
    @DisplayName("Quando eu informar dados novos para um CPF já cadastrado, Então o sistema deve substituir os dados já registrados")
    public void quandoAtualizarCliente_Sucesso(){
        Cliente cliente = new Cliente("101112", "Alterou Nome", "Alterou@email.com", "1758", "7", "false");
        postaCliente(cliente);
        atualizaCliente(cliente);
    }

    @Test
    @DisplayName("Quando eu informar dados novos para um CPF não existente, Então o sistema deve informar que ele não existe")
    public void quandoAtualizarCliente_Fracasso(){
        Cliente cliente = new Cliente("0", "Alterou Nome", "Alterou@email.com", "1758", "7", "false");

        String preMensagem = "CPF ";
        String posMensagem = " não encontrado";
        atualizaCliente(cliente)
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("mensagem", equalTo(preMensagem + cliente.getCpf() + posMensagem));
    }

    @Test
    @DisplayName("Quando eu solicitar todas as simulações cadastradas, Então deve ser exibida a lista de simulações cadastradas")
    public void quandoSolicitarTodasAsSimulacoes(){
        pegaTodosClientes()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Quando eu solicitar todas as simulações cadastradas e nao houver itens, Então deve ser gerado erro")
    public void quandoNaoHouverSimulacoes(){
        pegaTodosClientes()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Quando eu buscar uma solicitação por CPF, Então deve ser mostrado os dados associados ao CPF")
    public void quandoBuscarPorCpf_ApresentaDados(){
        Cliente cliente = new Cliente("12345678910", "Alterou Nome", "Alterou@email.com", "1758", "7", "false");

        postaCliente(cliente);
        pegaCliente(cliente)
                .body("cpf", equalTo(cliente.getCpf()));
    }

    @Test
    @DisplayName("Quando eu buscar por um CPF que não possui simulação, Então deve ser exibido mensagem de erro")
    public void quandoBuscaPorCpfSemSimulacao_GeraErro(){
        Cliente cliente = new Cliente("999", "Alterou Nome", "Alterou@email.com", "1758", "7", "false");

        apagaCliente(cliente);
        pegaCliente(cliente)
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Quando eu remover uma simulação, Então deve ser exibida mensagem de sucesso")
    public void quandoRemoverCPF_ApresentaSucesso(){
        Cliente cliente = new Cliente("12345678910", "Alterou Nome", "Alterou@email.com", "1758", "7", "false");

        postaCliente(cliente);
        apagaCliente(cliente)
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Quando eu remover uma simulação nao existente, Então deve ser exibida mensagem de erro")
    public void quandoRemoverNaoExistente_ExibeErro(){
        Cliente cliente = new Cliente("12345678910", "Alterou Nome", "Alterou@email.com", "1758", "7", "false");

        apagaCliente(cliente);
        apagaCliente(cliente)
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("mensagem", equalTo("Simulação não encontrada"));
    }

    // Métodos de apoio
    private ValidatableResponse postaCliente(Cliente cliente)  {
        return given()
                .contentType(ContentType.JSON)
                .body(cliente)
                .when().
                post(URL_SIMULACOES)
                .then();
    }

    private ValidatableResponse pegaTodosClientes() {
        return  given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL_SIMULACOES)
                .then();
    }

    private ValidatableResponse pegaCliente(Cliente cliente) {
        return  given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL_SIMULACOES + cliente.getCpf())
                .then();
    }

    private ValidatableResponse apagaCliente(Cliente cliente) {
        return  given()
                .contentType(ContentType.JSON)
                .when()
                .delete(URL_SIMULACOES + cliente.getCpf())
                .then();
    }

    private ValidatableResponse atualizaCliente(Cliente cliente) {
        return  given()
                .contentType(ContentType.JSON)
                .when()
                .put(URL_SIMULACOES + cliente.getCpf())
                .then();
    }
}
