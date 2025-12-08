
package entidade;

import java.util.ArrayList;

public class Passageiro extends Pessoa{
    
    private ArrayList<Pagamento> metodoPagamento = new ArrayList<>();
    boolean pedirCorrida;
    
     public Passageiro(String nome, String cpf, String email, String telefone,String senha,Pagamento p) {

        this.setNome(nome);
        this.setCpf(cpf);
        this.setEmail(email);
        this.setTelefone(telefone);
        this.setSenha(senha);
        metodoPagamento.add(p);
        pedirCorrida = true;
        
     }

    public Passageiro() {
        
    }

    public boolean isPedirCorrida() {
        return pedirCorrida;
    }

    public void setPedirCorrida(boolean pedirCorrida) {
        this.pedirCorrida = pedirCorrida;
    }
    
    public void setMetodoPagamento(Pagamento p) {
        metodoPagamento.add(p);
    }

    public ArrayList<Pagamento> getMetodoPagamento() {
        return metodoPagamento;
    }
    
}
