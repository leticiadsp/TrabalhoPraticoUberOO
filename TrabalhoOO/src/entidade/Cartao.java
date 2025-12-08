
package entidade;

import excecoes.PagamentoRecusadoException;


public class Cartao extends Pagamento {
    private String numeroCartao;
    private String dataVencimento;
    private int cvv;
    
    @Override
    public String getTipo() {
        return "CARTAO";
    }

    public Cartao(String numeroCartao, String dataVencimento, int cvv) {
        this.numeroCartao = numeroCartao;
        this.dataVencimento = dataVencimento;
        this.cvv = cvv;
    }
    
    
   @Override
    public void processar(float valor) throws PagamentoRecusadoException {

        if (numeroCartao == null || numeroCartao.trim().length() < 13) {
            setStatus("RECUSADO");
            throw new PagamentoRecusadoException(
                "Operadora recusou o pagamento: cartão inválido."
            );
        }

        if (cvv < 100 || cvv > 999) {
            setStatus("RECUSADO");
            throw new PagamentoRecusadoException(
                "Operadora recusou o pagamento: CVV inválido."
            );
        }

        setValor(valor);
        setStatus("PAGO");
    }

    
    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }
    
    
}
