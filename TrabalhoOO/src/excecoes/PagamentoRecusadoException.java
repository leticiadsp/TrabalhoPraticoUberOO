package excecoes;

public class PagamentoRecusadoException extends Exception {

    public PagamentoRecusadoException() {
        super("O pagamento foi recusado pelo provedor.");
    }

    public PagamentoRecusadoException(String msg) {
        super(msg);
    }
}


