
package entidade;

public class Motorista extends Pessoa {
    
    public StatusMotorista status;
    private Cnh cnh;
    public Veiculo veiculo;
   
    public Motorista(String nome, String cpf, String email, String telefone, Cnh cnh, Veiculo veiculo, String senha) {
         
        this.setNome(nome);
        this.setCpf(cpf);
        this.setEmail(email);
        this.setTelefone(telefone);
        this.setSenha(senha);
        this.cnh = cnh;
        this.veiculo = veiculo;
        status = StatusMotorista.ONLINE;
        
    }

    public Motorista() {
        
    }

    public StatusMotorista getStatus() {
        return status;
    }

    public void setStatus(StatusMotorista status) {
        this.status = status;
    }

    public Cnh getCnh() {
        return cnh;
    }

    public void setCnh(Cnh cnh) {
        this.cnh = cnh;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }
    
}
