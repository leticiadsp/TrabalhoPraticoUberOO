package entidade;
import excecoes.SaldoInsuficienteException;
import excecoes.PagamentoRecusadoException;

public class Dinheiro extends Pagamento {
    private float saldo;
    
    @Override
    public String getTipo() {
        return "DINHEIRO";
    }
       @Override
    public void processar(float valor) 
            throws SaldoInsuficienteException, PagamentoRecusadoException {

        if (saldo < valor) {
            setStatus("RECUSADO");
            throw new SaldoInsuficienteException(
                "Saldo insuficiente: saldo = " + saldo + ", valor = " + valor
            );
        }

        saldo -= valor;
        setValor(valor);
        setStatus("PAGO");
    }
    
    public Dinheiro(float saldo){
        this.saldo = saldo;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }
    


    
}
