package database;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Accounts {
    private String numero;
    private String saldo;

    public Accounts(String number, String balance) {
        this.numero = number;
        this.saldo = balance;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }
}
