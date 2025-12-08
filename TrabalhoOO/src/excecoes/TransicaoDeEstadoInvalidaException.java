
package excecoes;


public class TransicaoDeEstadoInvalidaException extends Exception {
     public TransicaoDeEstadoInvalidaException() {
        super("Transicao de estado invalida para a corrida.");
    }

    public TransicaoDeEstadoInvalidaException(String mensagem) {
        super(mensagem);
    }
}

 