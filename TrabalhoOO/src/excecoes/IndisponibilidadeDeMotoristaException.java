
package excecoes;

public class IndisponibilidadeDeMotoristaException extends Exception {

    public IndisponibilidadeDeMotoristaException() {
        super("Nenhum motorista disponivel no momento para atender a sua solicitacao.");
    }

    public IndisponibilidadeDeMotoristaException(String message) {
        super(message);
    }
}

