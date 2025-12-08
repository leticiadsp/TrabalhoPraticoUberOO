
package excecoes;


public class SaldoInsuficienteException extends Exception {
     public SaldoInsuficienteException() {
        super("Saldo insuficiente para realizar o pagamento.");
    }

    public SaldoInsuficienteException(String msg) {
        super(msg);
    }
}
