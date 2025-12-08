
package entidade;
import excecoes.PagamentoRecusadoException;
import excecoes.SaldoInsuficienteException;

public abstract class Pagamento {
    private float valor;
    private String status;
    private String tipo;
            
    public abstract String getTipo();
    
    public abstract void processar(float valor)
            throws PagamentoRecusadoException, SaldoInsuficienteException;

    public void setTipo(float valor) {
        this.valor = valor;
    }        
    
    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public boolean isPago() {
        return "PAGO".equalsIgnoreCase(status);
    }
    
}
