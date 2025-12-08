package servico;

import entidade.Corrida;
import entidade.Passageiro;
import entidade.Motorista;
import entidade.StatusMotorista;
import entidade.StatusCorrida;
import java.util.Scanner;
import static principal.Principal.cadCorrida;
import static principal.Principal.motoristaLogado;
import excecoes.IndisponibilidadeDeMotoristaException;
import excecoes.TransicaoDeEstadoInvalidaException;
import entidade.Pagamento;
import excecoes.PagamentoRecusadoException;
import excecoes.SaldoInsuficienteException;
import java.util.ArrayList;
import static principal.Principal.passageiroLogado;

public class CorridaServico {

    private static final Scanner sc = new Scanner(System.in);

    public static Corrida acharCorrida(Passageiro p) {
        for (int i = 0; i < cadCorrida.getLength(); i++) {
            Corrida c = cadCorrida.pesquisar(i);

            if (c.getPassageiro() == p) {
                return c;
            }

        }
        return null;
    }

    public static Corrida acharCorrida(Motorista m) {
        for (int i = 0; i < cadCorrida.getLength(); i++) {
            Corrida c = cadCorrida.pesquisar(i);

            if (c.getMotorista() == m
                    && c.getStatusCorrida() != StatusCorrida.FINALIZADA
                    && c.getStatusCorrida() != StatusCorrida.SOLICITADA) {
                return c;
            }
        }
        return null;
    }

    protected static void corridasDisponiveis() {

        LadoMotorista ldm = new LadoMotorista();
        System.out.println(" === Corridas Disponiveis === ");

        boolean temSolicitada = false;

        for (int i = 0; i < cadCorrida.getLength(); i++) {

            Corrida c = cadCorrida.pesquisar(i);

            if (c.getStatusCorrida() == StatusCorrida.SOLICITADA) {
                temSolicitada = true;

                System.out.println("[" + (i + 1) + "] "
                        + " | Origem: " + c.getOrigem()
                        + " | Destino: " + c.getDestino()
                        + " | Valor estimado: R$" + String.format("%.2f", c.getValorFinal()));

            }
        }

        if (!temSolicitada) {
            System.out.println("Nenhuma corrida solicitada no momento.");

        } else {

            System.out.println("Escolha uma corrida para aceitar (0 para voltar): ");
            int escolha = sc.nextInt();

            if (escolha == 0) {
                return;
            }

            if (escolha < 1 || escolha > cadCorrida.getLength()) {
                System.out.println("Opcao invalida!");
                return;
            }

            Corrida atual = acharCorrida(motoristaLogado);

            if (atual != null && atual.getStatusCorrida() == StatusCorrida.ACEITA) {
                System.out.println("Você já possui uma corrida em andamento.");

                ldm.menuGerenciarCorrida(atual);
                return;
            }

            Corrida corridaEscolhida = cadCorrida.pesquisar(escolha - 1);

            if (StatusCorrida.SOLICITADA == corridaEscolhida.getStatusCorrida()) {
                corridaEscolhida.setMotorista(motoristaLogado);

                corridaEscolhida.setStatusCorrida(StatusCorrida.ACEITA);
                motoristaLogado.setStatus(StatusMotorista.EM_CORRIDA);

                System.out.println("Corrida aceita com sucesso!");
                ldm.menuGerenciarCorrida(corridaEscolhida);
            } else {
                System.out.println("Opcao invalida!");
            }
        }
    }

    private static float calcularPreco(String categoria, float distanciaKm) {
        float tarifaBase = 0;
        float multiplicador = 0;

        if (categoria.equalsIgnoreCase("Comum")) {
            tarifaBase = 5;
            multiplicador = 1;
        } else if (categoria.equalsIgnoreCase("Luxo")) {
            tarifaBase = 9;
            multiplicador = 2.2f;
        }

        return tarifaBase + (multiplicador * distanciaKm);
    }

    private static Motorista encontrarMotoristaDisponivel(CadastroServico<Motorista> cadMotorista)
            throws IndisponibilidadeDeMotoristaException {
        for (int i = 0; i < cadMotorista.getLength(); i++) {
            Motorista m = cadMotorista.pesquisar(i);
            if (m.getStatus() != null && m.getStatus() == StatusMotorista.ONLINE) {
                return m;
            }
        }
        throw new IndisponibilidadeDeMotoristaException();
    }

    public static Corrida solicitarCorrida(
            Passageiro passageiro,
            String origem,
            String destino,
            String categoria,
            float distancia,
            CadastroServico<Motorista> cadMotorista
    ) throws IndisponibilidadeDeMotoristaException {

        Motorista m = encontrarMotoristaDisponivel(cadMotorista);

        if (m == null) {
            throw new IndisponibilidadeDeMotoristaException("Nenhum motorista disponível no momento.");
        }

        float valor = calcularPreco(categoria, distancia);

        return new Corrida(
                passageiro,
                m,
                distancia,
                categoria,
                StatusCorrida.SOLICITADA, 
                origem,
                destino,
                valor
        );
    }

    public static void aceitarCorrida(Corrida corrida) throws TransicaoDeEstadoInvalidaException {

        if (corrida.getStatusCorrida() != StatusCorrida.SOLICITADA) {
            throw new TransicaoDeEstadoInvalidaException(
                    "So e possivel aceitar corridas que estejam SOLICITADAS."
            );
        }

        corrida.setStatusCorrida(StatusCorrida.ACEITA);
        corrida.getMotorista().setStatus(StatusMotorista.EM_CORRIDA);
        System.out.println("Corrida aceita por " + corrida.getMotorista().getNome());
    }

    public static void iniciarViagem(Corrida corrida)
            throws TransicaoDeEstadoInvalidaException {

        if (corrida.getStatusCorrida() != StatusCorrida.ACEITA) {
            throw new TransicaoDeEstadoInvalidaException(
                    "So e possivel iniciar uma corrida que esteja ACEITA."
            );
        }

        corrida.setStatusCorrida(StatusCorrida.EM_ANDAMENTO);
        System.out.println("Corrida em andamento...");
    }

    public static void finalizarCorrida(Corrida corrida)
            throws TransicaoDeEstadoInvalidaException {

        if (corrida.getStatusCorrida() != StatusCorrida.EM_ANDAMENTO) {
            throw new TransicaoDeEstadoInvalidaException(
                    "Nao e possivel finalizar uma corrida que nao esta EM_ANDAMENTO."
            );
        }

        corrida.setStatusCorrida(StatusCorrida.FINALIZADA);
        System.out.println("Corrida finalizada! Aguardando pagamento...");

        // motorista pode voltar a ficar online
        corrida.getMotorista().setStatus(StatusMotorista.ONLINE);
    }

    public static boolean processarPagamentoEFeedback(Corrida corrida) {

        Passageiro passageiro = corrida.getPassageiro();
        ArrayList<Pagamento> metodos = passageiro.getMetodoPagamento();

        if (metodos == null || metodos.isEmpty()) {
            System.out.println("Voce nao possui nenhum metodo de pagamento cadastrado.");
            return false;
        }

        Pagamento metodo = metodos.get(0); // método padrão
        float valor = corrida.getValorFinal();

        System.out.println("Processando pagamento via " + metodo.getTipo()
                + " no valor de R$ " + String.format("%.2f", valor));

        try {
            metodo.processar(valor);

            System.out.println("Pagamento aprovado!");
            System.out.println("Status do pagamento: " + metodo.getStatus());

            coletarFeedback(corrida);

            return true;

        } catch (SaldoInsuficienteException e) {
            System.out.println("Pagamento recusado: saldo insuficiente.");
            System.out.println(e.getMessage());
            passageiroLogado.setPedirCorrida(false);
            return false;

        } catch (PagamentoRecusadoException e) {
            System.out.println("Pagamento recusado pela operadora.");
            System.out.println(e.getMessage());
            passageiroLogado.setPedirCorrida(false);
            return false;
        }
    }

    private static void coletarFeedback(Corrida corrida) {

        System.out.println("Avalie o motorista " + corrida.getMotorista().getNome() + " (1-5): ");
        int notaMotorista = sc.nextInt();
        corrida.getMotorista().atualizarFeedback(notaMotorista);

        System.out.println("Avalie o passageiro " + corrida.getPassageiro().getNome() + " (1-5): ");
        int notaPassageiro = sc.nextInt();
        corrida.getPassageiro().atualizarFeedback(notaPassageiro);

        System.out.println("Feedback registrado com sucesso!");
    }

    public static void cancelarCorrida(Corrida corrida, Motorista mt) throws TransicaoDeEstadoInvalidaException {

        if (corrida.getStatusCorrida() == StatusCorrida.EM_ANDAMENTO) {
            throw new TransicaoDeEstadoInvalidaException(
                    " Nao e possivel cancelar uma corrida que ja foi iniciada!"
            );
        } else {

            mt.setStatus(StatusMotorista.ONLINE);
            cadCorrida.remover(corrida);
            System.out.println("Corrida cancelada com sucesso!");
            
        }

    }

}
