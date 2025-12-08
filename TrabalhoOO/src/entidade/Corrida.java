
package entidade;

public class Corrida {
    private Passageiro passageiro;
    private Motorista motorista;
    private float distancia;
    private String categoria;
    private StatusCorrida statusCorrida;
    private String destino;
    private String origem;
    private float valorFinal;

    public Corrida(Passageiro passageiro, Motorista motorista, float distancia, String categoria,
               StatusCorrida statusCorrida, String destino, String origem, float valorFinal) {
        this.passageiro = passageiro;
        this.motorista = motorista;
        this.distancia = distancia;
        this.categoria = categoria;
        this.statusCorrida = statusCorrida;
        this.destino = destino;
        this.origem = origem;
        this.valorFinal = valorFinal;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }


    public Passageiro getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(Passageiro passageiro) {
        this.passageiro = passageiro;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public StatusCorrida getStatusCorrida() {
        return statusCorrida;
    }

    public void setStatusCorrida(StatusCorrida statusCorrida) {
        this.statusCorrida = statusCorrida;
    }


    public float getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(float valorFinal) {
        this.valorFinal = valorFinal;
    }
     
}
