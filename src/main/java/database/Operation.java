package database;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Operation {
    private String id;
    private String operacao;
    private String conta;
    private String valor;

    public Operation(){}

    public Operation(String id, String operacao, String conta, String valor) {
        this.id = id;
        this.operacao = operacao;
        this.conta = conta;
        this.valor = valor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
