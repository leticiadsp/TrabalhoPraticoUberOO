package servico;

import entidade.Cartao;
import entidade.Corrida;
import entidade.Dinheiro;
import entidade.Motorista;
import entidade.Pagamento;
import entidade.Passageiro;
import entidade.Pix;
import entidade.StatusCorrida;
import static entidade.StatusCorrida.FINALIZADA;
import static entidade.StatusCorrida.SOLICITADA;
import entidade.StatusMotorista;
import excecoes.CadastroInvalidoException;
import java.util.ArrayList;
import static principal.Principal.cadCorrida;
import static principal.Principal.cadMotorista;
import static principal.Principal.cadPassageiro;
import static principal.Principal.limpar;
import static principal.Principal.passageiroLogado;
import excecoes.IndisponibilidadeDeMotoristaException;
import excecoes.PagamentoRecusadoException;
import excecoes.TransicaoDeEstadoInvalidaException;
import static principal.Principal.cadPassageiro;
import static principal.Principal.motoristaLogado;

/**
 *
 * @author Clara
 */
public class LadoPassageiro implements Menus {

    private Pagamento cadMetodoPagamento() {

        System.out.println("=== Escolha um metodo de pagamento ===");
        System.out.println("[1] - Pix");
        System.out.println("[2] - Dinheiro");
        System.out.println("[3] - Cartao de Credito");
        System.out.print("Opcao: ");
        int opc = sc.nextInt();
        String chave;

        switch (opc) {
            case 1:
                System.out.print("Chave Pix: ");
                chave = sc.next();
                return new Pix(chave);
            case 2:
                System.out.print("Saldo: R$ ");
                float saldo = sc.nextFloat();
                return new Dinheiro(saldo);
            case 3:
                return cadastrarCartaoViaInput();
            default:
                System.out.println("Opcao invalida, usando Pix como padrao.");
                System.out.print("Chave Pix: ");
                chave = sc.next();
                return new Pix(chave);
        }

    }

    private Cartao cadastrarCartaoViaInput() {

        System.out.println("=== Cadastro Cartao ===");

        System.out.print("Numero do cartao: ");
        String numero = sc.next();

        System.out.print("Validade (MM/AA): ");
        String dtVencimento = sc.next();

        System.out.print("CVV: ");
        int cvv = sc.nextInt();

        return new Cartao(numero, dtVencimento, cvv);
    }

    @Override
    public void cadastrar() {

        System.out.println("===== Cadastro Passageiro =====");

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
            validarCadastroPassageiro(cpf, email);
        } catch (CadastroInvalidoException e) {
            System.out.println("Erro no cadastro: " + e.getMessage());
            return;
        }

        Pagamento metodo = cadMetodoPagamento();
        Passageiro p = new Passageiro(nome, cpf, email, telefone, senha, metodo);

        if (cadPassageiro.adicionar(p)) {
            System.out.println("Passageiro cadastrado com sucesso!");
        } else {
            System.out.println("Erro ao cadastrar passageiro!");
        }

        limpar();
    }

    @Override
    public void menuInicial() {

        int opcao;

        Corrida atual = CorridaServico.acharCorrida(passageiroLogado);

        if (atual != null && atual.getStatusCorrida() != StatusCorrida.FINALIZADA) {
            menuGerenciarCorrida(atual);
            return;
        }

        do {

            if (passageiroLogado == null) {
                System.out.println("Voce foi deslogado. Voltando ao menu principal...");
                limpar();
                return;
            }

            System.out.println("=== Menu Passageiro ===");
            System.out.println("[1] - Pedir corrida");
            System.out.println("[2] - Pagamento padrao");
            System.out.println("[3] - Cadastrar novo metodo de pagamento");
            System.out.println("[4] - Saldo");
            System.out.println("[5] - Voltar");

            System.out.print("Escolha uma opcao: ");
            opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    if (passageiroLogado.isPedirCorrida() == false) {

                        System.out.println("ERRO: voce tem uma corrida com pagamento pendente");
                        menuGerenciarCorrida(atual);
                    } else {

                        System.out.println("=== Corrida ===");

                        sc.nextLine();

                        System.out.print("Informe o local de partida: ");
                        String origem = sc.nextLine();

                        System.out.print("Informe o destino: ");
                        String destino = sc.nextLine();

                        System.out.println("Categoria:");
                        System.out.println("[1] - Comum");
                        System.out.println("[2] - Luxo");
                        System.out.print("Escolha: ");
                        String c = sc.next();

                        String categoria = "Comum";
                        if (c.equalsIgnoreCase("2")) {
                            categoria = "Luxo";
                        }

                        System.out.print("Distancia estimada (km): ");
                        float distancia = sc.nextFloat();

                        try {
                            Corrida ca = CorridaServico.solicitarCorrida(
                                    passageiroLogado,
                                    origem,
                                    destino,
                                    categoria,
                                    distancia,
                                    cadMotorista
                            );
                            cadCorrida.adicionar(ca);
                            System.out.println("Corrida solicitada com sucesso!");
                            menuGerenciarCorrida(ca);

                        } catch (IndisponibilidadeDeMotoristaException e) {
                            System.out.println(e.getMessage());
                        }

                    }
                    break;

                case 2:

                    gerenciarPagamentoPadrao(passageiroLogado);
                    break;

                case 3:

                    Pagamento metodo = cadMetodoPagamento();

                    ArrayList<Pagamento> lista = passageiroLogado.getMetodoPagamento();

                    String tipoNovo = metodo.getTipo();

                    boolean existePix = false;
                    boolean existeDinheiro = false;

                    for (Pagamento p : lista) {
                        if (p.getTipo().equals("DINHEIRO")) {
                            existeDinheiro = true;
                        }
                    }

                    if (tipoNovo.equals("DINHEIRO") && existeDinheiro) {
                        System.out.println("Voce ja possui um metodo DINHEIRO cadastrado!");

                    } else {
                        passageiroLogado.setMetodoPagamento(metodo);
                        System.out.println("Metodo de pagamento adicionado com sucesso!");
                    }
                    break;
                case 4:

                    ArrayList<Pagamento> mp = passageiroLogado.getMetodoPagamento();

                    Dinheiro dinheiro = null;

                    for (Pagamento p : mp) {
                        if (p.getTipo().equals("DINHEIRO")) {
                            dinheiro = (Dinheiro) p; // Coerção
                            break;
                        }
                    }

                    if (dinheiro == null) {
                        System.out.println("Voce nao possui metodo de pagamento Dinheiro.");
                    } else {
                        System.out.println("==== Saldo ====");
                        System.out.println("Seu saldo atual: R$ " + dinheiro.getSaldo());
                        System.out.println("Deseja depositar algum valor:");
                        System.out.println("[1]- Sim [2]- Nao");
                        int m = sc.nextInt();

                        if (1 == m) {
                            System.out.print("Valor para depositar: ");
                            float valor = sc.nextFloat();
                            if (valor >= 0) {
                                dinheiro.setSaldo(dinheiro.getSaldo() + valor);
                                System.out.println("Deposito realizado! \nNovo saldo: R$ " + dinheiro.getSaldo());
                            } else {
                                System.out.println("Valor invalido!");
                            }
                        }
                    }
                    break;

                case 5:
                    System.out.println("Voltando ao menu anterior...");
                    passageiroLogado = null;
                    break;

                default:
                    System.out.println("Opcao invalida!");
            }

            System.out.println();
        } while (opcao != 5);

    }

    @Override
    public void menuGerenciarCorrida(Corrida corrida) {

        limpar();

        int opc;

        do {
            System.out.println("\n=== Sua Corrida ===");
            System.out.println("Origem: " + corrida.getOrigem());
            System.out.println("Destino: " + corrida.getDestino());
            System.out.println("Status: " + corrida.getStatusCorrida());
            System.out.println("Preco: R$" + String.format("%.2f", corrida.getValorFinal()));

            if (corrida.getStatusCorrida() != StatusCorrida.SOLICITADA) {

                Motorista m = corrida.getMotorista();

                if (m != null) {
                    System.out.print("Motorista: " + m.getNome());
                    if (m.getVeiculo() != null) {
                        System.out.print(" - " + m.getVeiculo());
                    }
                    System.out.println();
                }
            } else {
                System.out.println("Motorista: aguardando aceitacao...");
            }

            switch (corrida.getStatusCorrida()) {
                case SOLICITADA -> {
                    System.out.println("[1] Cancelar solicitacao");
                    System.out.println("[2] Voltar");
                }
                case FINALIZADA -> {
                    System.out.println("[1] Pagar");
                    System.out.println("[2] Voltar");
                }
                default -> {
                    System.out.println("[1] Voltar");
                }
            }

            System.out.print("Escolha: ");
            opc = sc.nextInt();

            switch (corrida.getStatusCorrida()) {
                case SOLICITADA -> {
                    if (opc == 1) {

                        Motorista mt = corrida.getMotorista();

                        try {
                            CorridaServico.cancelarCorrida(corrida, mt);
                            return;
                        } catch (TransicaoDeEstadoInvalidaException e) {
                            System.out.println(e.getMessage());
                        }

                        return;
                    } else if (opc == 2) {
                        passageiroLogado = null;
                        return;
                    } else {
                        System.out.println("Opcao invalida.");
                    }
                }
                case FINALIZADA -> {
                    if (opc == 1) {

                        boolean pago = CorridaServico.processarPagamentoEFeedback(corrida);

                        if (pago) {
                            cadCorrida.remover(corrida);
                            passageiroLogado = null;
                            return;
                        } else {
                            System.out.println("Nao foi possivel realizar o pagamento.");
                            System.out.println("[1] Tentar novamente");
                            System.out.println("[2] Alterar metodo de pagamento");
                            System.out.println("[3] Voltar ao menu do passageiro");
                            System.out.print("Escolha: ");
                            int opcErro = sc.nextInt();

                            if (opcErro == 1) {
                                continue;
                            } else if (opcErro == 2) {
                                gerenciarPagamentoPadrao(passageiroLogado);
                                continue;
                            } else if (opcErro == 3) {
                                menuInicial(); 
                                return;
                            } else {
                                System.out.println("Opcao invalida.");
                                continue;
                            }
                        }

                    } else if (opc == 2) {
                        menuInicial();
                        return;

                    } else {
                        System.out.println("Opcao invalida.");
                    }
                }

            }

        } while (true);
    }

    private void gerenciarPagamentoPadrao(Passageiro passageiro) {
        try {
            ArrayList<Pagamento> mp = passageiro.getMetodoPagamento();
            if (mp.isEmpty()) {
                throw new PagamentoRecusadoException("Voce nao possui nenhum metodo de pagamento cadastrado.");
            }

            Pagamento padrao = mp.get(0);
            System.out.println("=== Pagamento Padrao ===");
            System.out.println("Tipo: " + padrao.getTipo());

            switch (padrao.getTipo()) {
                case "DINHEIRO":
                    System.out.println("Saldo: R$ " + ((Dinheiro) padrao).getSaldo());
                    break;
                case "CARTAO":
                    Cartao cartao = (Cartao) padrao;
                    System.out.println("Numero: " + cartao.getNumeroCartao()
                            + " | Data: " + cartao.getDataVencimento());
                    break;
                case "PIX":
                    Pix pix = (Pix) padrao;
                    System.out.println("Chave Pix: " + pix.getChave());
                    break;
            }

            System.out.println("\nDeseja alterar o pagamento padrao?");
            System.out.println("[1] - Sim");
            System.out.println("[2] - Nao");
            int opc = sc.nextInt();
            if (opc != 1) {
                return;
            }

            if (mp.size() == 1) {
                throw new PagamentoRecusadoException("Voce so possui um metodo de pagamento. Nao e possivel alterar o padrao.");
            }

            System.out.println("=== Seus Metodos de Pagamento ===");
            for (int i = 1; i < mp.size(); i++) {

                Pagamento p = mp.get(i); 

                System.out.print("[" + i + "] - " + p.getTipo());

                if (p.getTipo().equals("PIX")) {
                    Pix px = (Pix) p;
                    System.out.print(" - Chave: " + px.getChave());
                } else if (p.getTipo().equals("CARTAO")) {
                    Cartao c = (Cartao) p;
                    System.out.print(" - Número do cartao: " + c.getNumeroCartao());
                } else if (p.getTipo().equals("DINHEIRO")) {
                    Dinheiro d = (Dinheiro) p;
                    System.out.print(" - Saldo: R$ " + d.getSaldo());
                }

                System.out.println();
            }

            System.out.print("Escolha o novo pagamento padrao: ");
            int novo = sc.nextInt();

            if (novo < 1 || novo >= mp.size()) {
                throw new PagamentoRecusadoException("Opcao invalida! Escolha um indice listado.");
            }

            Pagamento escolhido = mp.remove(novo);
            mp.add(0, escolhido);

            System.out.println("Pagamento padrao atualizado para: " + escolhido.getTipo());

        } catch (PagamentoRecusadoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void validarCadastroPassageiro(String cpf, String email) throws CadastroInvalidoException {

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
                for (int i = 0; i < cadPassageiro.getLength(); i++) {
                    Passageiro p = cadPassageiro.pesquisar(i);
                    if (p != null && p.getEmail() != null
                            && p.getEmail().equalsIgnoreCase(email)) {
                        erros.add("Ja existe um passageiro com esse email.");
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

}
