package servico;

import entidade.Cnh;
import entidade.Corrida;
import entidade.Motorista;
import entidade.StatusCorrida;
import entidade.StatusMotorista;
import entidade.Veiculo;
import excecoes.CadastroInvalidoException;
import static principal.Principal.cadCorrida;
import static principal.Principal.cadMotorista;
import static principal.Principal.motoristaLogado;
import static principal.Principal.limpar;
import excecoes.TransicaoDeEstadoInvalidaException;
import static principal.Principal.cadMotorista;
import java.util.ArrayList;


public class LadoMotorista implements Menus {

   @Override
    public void cadastrar() {

    System.out.println("===== Cadastro Motorista =====");

    System.out.print("Nome: ");
    String nome = sc.nextLine();

    System.out.print("CPF: ");
    String cpf = sc.next();

    System.out.print("Telefone: ");
    String telefone = sc.next();

    System.out.print("Email: ");
    String email = sc.next();

    System.out.print("Senha: ");
    String senha = sc.next();

    try {
        validarEmail(email);
        validarCadastroMotorista(cpf, email);
    } catch (CadastroInvalidoException e) {
        System.out.println("Erro no cadastro: " + e.getMessage());
        return; 
    }

    System.out.print("Numero da CNH: ");
    String numeroCnh = sc.next();

    System.out.print("Data de validade da CNH (dd/mm/aaaa): ");
    String validadeCnh = sc.next();

    Cnh cnh = new Cnh(numeroCnh, validadeCnh);

    System.out.print("Cor do veiculo: ");
    String cor = sc.next();

    System.out.print("Placa do veiculo: ");
    String placa = sc.next();

    System.out.print("Ano do veiculo: ");
    int ano = sc.nextInt();

    System.out.print("Modelo do veiculo: ");
    String modelo = sc.next();

    Veiculo veiculo = new Veiculo(cor, placa, ano, modelo);

    Motorista m = new Motorista(nome, cpf, email, telefone, cnh, veiculo, senha);

    if (cadMotorista.adicionar(m)) {
        System.out.println("Motorista cadastrado com sucesso!");
    } else {
        System.out.println("Erro ao cadastrar motorista!");
    }

    limpar();
}

    @Override
    public void menuInicial() {

        int opcao;

        Corrida atual = CorridaServico.acharCorrida(motoristaLogado);

        if ((atual != null) && (atual.getStatusCorrida() == StatusCorrida.ACEITA
                || atual.getStatusCorrida() == StatusCorrida.EM_ANDAMENTO)) {
            menuGerenciarCorrida(atual);
        }

        do {

            if (motoristaLogado == null) {
                System.out.println("Voce foi deslogado. Voltando ao menu principal...");
                limpar();
                return;
            }
            System.out.println("=== Menu Motorista ===\nStatus: " + motoristaLogado.getStatus());
            System.out.println("[1] - Corridas disponiveis");
            System.out.println("[2] - Mudar status");
            System.out.println("[3] - Voltar");

            System.out.print("Escolha uma opcao: ");
            opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    if (motoristaLogado.getStatus() == StatusMotorista.ONLINE) {
                        CorridaServico.corridasDisponiveis();
                    } else {
                        System.out.println("Voce nao esta online!");
                    }
                    break;
                case 2:
                    if (motoristaLogado.getStatus() == StatusMotorista.ONLINE) {
                        motoristaLogado.setStatus(StatusMotorista.OFFLINE);
                    } else {
                        motoristaLogado.setStatus(StatusMotorista.ONLINE);
                    }
                    break;
                case 3:
                    motoristaLogado = null;
                    return;
                default:
                    System.out.println("Opcao invalida!");
            }

            System.out.println();
        } while (true);
    }

    @Override
    public void menuGerenciarCorrida(Corrida corrida) {

        int menu;

        do {
            System.out.println("=== Corrida ===");
            System.out.println("Passageiro(a):  " + corrida.getPassageiro().getNome());
            System.out.println("Origem: " + corrida.getOrigem());
            System.out.println("Destino: " + corrida.getDestino());
            System.out.println("Status: " + corrida.getStatusCorrida());

            menu = menuCorridaAceita();

            if (corrida.getStatusCorrida() == StatusCorrida.ACEITA
                    || corrida.getStatusCorrida() == StatusCorrida.EM_ANDAMENTO) {

                switch (menu) {
                    case 1:
                        try {
                            CorridaServico.iniciarViagem(corrida);
                        } catch (TransicaoDeEstadoInvalidaException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 2:
                        try {
                            CorridaServico.finalizarCorrida(corrida);
                        } catch (TransicaoDeEstadoInvalidaException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 3:
                        try {
                            CorridaServico.cancelarCorrida(corrida, motoristaLogado);
                            return;
                        } catch (TransicaoDeEstadoInvalidaException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 4:
                        motoristaLogado = null;
                        return;
                    default:
                        System.out.println("Numero invalido");
                        break;
                }
            } else if (corrida.getStatusCorrida() == StatusCorrida.FINALIZADA) {

                System.out.println("Corrida finalizada! Retornando ao menu...");
                return;

            } else {
                return;
            }

        } while (true);
    }

    private int menuCorridaAceita() {

        System.out.println("[1] - Iniciar corrida");
        System.out.println("[2] - Finalizar corrida");
        System.out.println("[3] - Cancelar corrida");
        System.out.println("[4] - Voltar");
        int menu = sc.nextInt();
        return menu;
    }
   private void validarCadastroMotorista(String cpf, String email) throws CadastroInvalidoException {

    ArrayList<String> erros = new ArrayList<>();

    // CPF
    if (cpf == null) {
        erros.add("CPF nao pode ser nulo.");
    } else {
        String cpfLimpo = cpf.replaceAll("\\D", "");
        if (cpfLimpo.length() != 11) {
            erros.add("CPF deve ter exatamente 11 digitos.");
        }
    }

    if (email != null && !email.isBlank()) {

        if (!email.contains("@") || !email.contains(".")) {
            erros.add("Formato de email invalido.");
        } else {
            for (int i = 0; i < cadMotorista.getLength(); i++) {
                Motorista m = cadMotorista.pesquisar(i);
                if (m != null && m.getEmail() != null &&
                        m.getEmail().equalsIgnoreCase(email)) {
                    erros.add("Ja existe um motorista com esse email.");
                    break;
                }
            }
        }
    }

    if (!erros.isEmpty()) {
        String mensagem = "Erros no cadastro:\n";
        for (String e : erros) {
            mensagem += "- " + e + "\n";
        }
        throw new CadastroInvalidoException(mensagem);
    }
}

    private void validarEmail(String email) throws CadastroInvalidoException {

    if (email == null || email.isBlank()) {
        return;
    }

    if (!email.contains("@") || !email.contains(".")) {
        throw new CadastroInvalidoException("Formato de email invalido.");
    }
}



}
