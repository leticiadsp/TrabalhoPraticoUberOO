package entidade;

public abstract class Pessoa {

    public String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String senha;
    private float media_feedback;
    private int totalAvaliacoes = 0;

    public void atualizarFeedback(int nota) {
        float total = media_feedback * totalAvaliacoes;
        totalAvaliacoes++;
        media_feedback = (total + nota) / totalAvaliacoes;
    }

    public Pessoa() {
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public float getMedia_feedback() {
        return media_feedback;
    }

    public void setMedia_feedback(float media_feedback) {
        this.media_feedback = media_feedback;
    }

}
