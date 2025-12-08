package principal;

import entidade.Cartao;
import entidade.Cnh;
import entidade.Dinheiro;
import java.util.Scanner;
import entidade.Motorista;
import entidade.Pagamento;
import entidade.Passageiro;
import entidade.Pix;
import entidade.Veiculo;
import servico.CadastroServico;
import entidade.Corrida;
import entidade.StatusMotorista;
import servico.CorridaServico;
import servico.LadoMotorista;
import servico.LadoPassageiro;
import entidade.StatusCorrida;
import excecoes.TransicaoDeEstadoInvalidaException;



public class Principal {

    private static final Scanner sc = new Scanner(System.in);

    public static CadastroServico<Passageiro> cadPassageiro = new CadastroServico<>();
    public static CadastroServico<Motorista> cadMotorista = new CadastroServico<>();
    public static CadastroServico<Corrida> cadCorrida = new CadastroServico<>();

    public static Passageiro passageiroLogado = null;
    public static Motorista motoristaLogado = null;
        
    public static void main(String[] args) {

        int menu;

        Pagamento pagamentoTeste = new Pix("90028922"); 
        Passageiro passageiroTeste = new Passageiro(
                "Ana Clara", "000.000.000-00", "ana", "11999999999", "123", pagamentoTeste);
        cadPassageiro.adicionar(passageiroTeste);

        Cnh cnhTeste = new Cnh("12345678", "12/25");
        Veiculo veiculoTeste = new Veiculo("Prata", "ABC-1234", 2020, "HB20");
        Motorista motoristaTeste = new Motorista(
                "Joao", "111.111.111-11", "joao", "11988888888", cnhTeste, veiculoTeste, "123");
        motoristaTeste.setStatus(StatusMotorista.ONLINE); 
        cadMotorista.adicionar(motoristaTeste);
        
        

        do {
            menu = menuInicial();
            switch (menu) {
                case 1:

                    int a = menuLog();
                   if (a == 1) {
                        //passageiro
                        LadoPassageiro lp = new LadoPassageiro();
                        login(lp);
                    } else if (a == 2) {
                        //motorista 
                        LadoMotorista lm = new LadoMotorista();
                        login(lm);
                    }
                    break;
                case 2:
                    int v = menuCad();

                    if (v == 1) {
                        //passageiro
                        LadoPassageiro lp = new LadoPassageiro();
                        lp.cadastrar();
                    } else if (v == 2) {
                        //motorista 
                        LadoMotorista ld = new LadoMotorista();
                        ld.cadastrar();
                    }
                    break;
                case 3:
                    System.out.println("Saindo do programa...");
                    break;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while (menu != 3);

        System.out.println("=== Passageiros cadastrados ===");
        for (int i = 0; i < cadPassageiro.getLength(); i++) {
            Passageiro p = cadPassageiro.pesquisar(i);
            System.out.println("Nome: " + p.getNome() + ", CPF: " + p.getCpf() + ", Email: " + p.getEmail());
        }

        System.out.println("=== Motoristas cadastrados ===");
        for (int i = 0; i < cadMotorista.getLength(); i++) {
            Motorista p = cadMotorista.pesquisar(i);
            System.out.println("Nome: " + p.getNome() + ", CPF: " + p.getCpf() + ", Email: " + p.getEmail());
        }

    }

    // Menu principal
    
    private static int menuInicial() {

        System.out.println("\n=== Menu ===");
        System.out.println("[1] - Login");
        System.out.println("[2] - Cadastrar");
        System.out.println("[3] - Sair");
        System.out.print("Escolha uma opcao: ");
        return sc.nextInt();
    }
    private static int menuCad() {

        System.out.println("\n=== Selecione a categoria para cadastro: ===");
        System.out.println("[1] - Passageiro");
        System.out.println("[2] - Motorista");
        System.out.println("[3] - Voltar");
        System.out.print("Escolha uma opcao: ");
        return sc.nextInt();
    }
    private static int menuLog() {

        System.out.println("\n=== Selecione a categoria para login: ===");
        System.out.println("[1] - Passageiro");
        System.out.println("[2] - Motorista");
        System.out.println("[3] - Voltar");
        System.out.print("Escolha uma opcao: ");
        return sc.nextInt();
    }

    //Sobre carga de metodos
    private static void login(LadoMotorista lm) {

        System.out.println("=== Login ===");
        System.out.print("E-mail: ");
        String email = sc.next();
        System.out.print("Senha: ");
        String senha = sc.next();

        boolean encontrou = false;

        if (!encontrou) {
            for (int i = 0; i < cadMotorista.getLength(); i++) {
                Motorista m = cadMotorista.pesquisar(i);
                if (m.getEmail().equals(email) && m.getSenha().equals(senha)) {
                    System.out.println("Login realizado com sucesso!");
                    System.out.println("Bem-vindo(a), motorista " + m.getNome() + "!");
                    
                    motoristaLogado = m;
                    passageiroLogado = null;
                    
                    encontrou = true;
                    limpar();
                    Corrida c = CorridaServico.acharCorrida(motoristaLogado);

                    if (c != null) {
                    lm.menuGerenciarCorrida(c);
                    } else {
                    lm.menuInicial();
                    }

                    break;
                }
            }
        }

        if (!encontrou) {
            System.out.println("E-mail ou senha invalidos. Tente novamente.");
        }
    }
    
    private static void login(LadoPassageiro lp) {

        System.out.println("=== Login ===");
        System.out.print("E-mail: ");
        String email = sc.next();
        System.out.print("Senha: ");
        String senha = sc.next();

        boolean encontrou = false;

        for (int i = 0; i < cadPassageiro.getLength(); i++) {
            Passageiro p = cadPassageiro.pesquisar(i);
            if (p.getEmail().equals(email) && p.getSenha().equals(senha)) {
                System.out.println("Login realizado com sucesso!");
                System.out.println("Bem-vindo(a), passageiro(a) " + p.getNome() + "!");
                passageiroLogado = p;
                motoristaLogado = null;
                encontrou = true;
                limpar();
                Corrida c = CorridaServico.acharCorrida(passageiroLogado);

                if (c != null) {
                    lp.menuGerenciarCorrida(c);
                } else {
                    lp.menuInicial();
                }

                break;
            }
            }
     
        if (!encontrou) {
            System.out.println("E-mail ou senha invalidos. Tente novamente.");
        }
    }
    
    //Limpar
    public static void limpar() {
        for (int i = 0; i < 30; i++) {
            System.out.println();

        }

    }

}
