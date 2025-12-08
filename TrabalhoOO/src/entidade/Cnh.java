
package entidade;
public class Cnh {
    private String numero;
    private String dataDeValidade;

    public Cnh(String numero, String dataDeValidade) {
        this.numero = numero;
        this.dataDeValidade = dataDeValidade;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDataDeValidade() {
        return dataDeValidade;
    }

    public void setDataDeValidade(String dataDeValidade) {
        this.dataDeValidade = dataDeValidade;
    }
    
}
