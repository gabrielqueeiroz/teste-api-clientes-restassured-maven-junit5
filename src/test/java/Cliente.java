public class Cliente {
    private String cpf, nome, email, valor, parcela, seguro;

    public Cliente(){
    }
    public Cliente(String cpf){
        this.cpf = cpf;
    }
    public Cliente(String cpf, String nome, String email, String valor, String parcela, String seguro){
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.valor = valor;
        this.parcela = parcela;
        this.seguro = seguro;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getParcela() {
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    public String getSeguro() {
        return seguro;
    }

    public void setSeguro(String seguro) {
        this.seguro = seguro;
    }
}
