package entidade;

import excecoes.PagamentoRecusadoException;
import excecoes.SaldoInsuficienteException;

public class Pix extends Pagamento {

    private String chave;

    @Override
    public String getTipo() {
        return "PIX";
    }

    @Override
    public void processar(float valor)
            throws SaldoInsuficienteException, PagamentoRecusadoException {
        
        if (chave == null || chave.isBlank()) {
            setStatus("RECUSADO");
            throw new PagamentoRecusadoException("Chave Pix inválida.");
        }

        if (chave.length() < 5) {
            setStatus("RECUSADO");
            throw new PagamentoRecusadoException("Chave Pix muito curta.");
        }

        setStatus("PAGO");
    }

    public Pix() {

    }

    public Pix(String ch) {
        chave = ch;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

}
