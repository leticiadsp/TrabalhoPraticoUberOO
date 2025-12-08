package entidade;

public class Veiculo {

    private String cor;
    private String placa;
    private int ano;
    private String modelo;

    public Veiculo(String cor, String placa, int ano, String modelo) {
        this.cor = cor;
        this.placa = placa;
        this.ano = ano;
        this.modelo = modelo;
    }

    @Override
    public String toString() {
        return modelo + " " + cor + " " + placa;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}
