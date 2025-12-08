package servico;

import entidade.Corrida;
import entidade.Pagamento;
import entidade.Passageiro;
import entidade.StatusCorrida;
import excecoes.PagamentoRecusadoException;
import excecoes.SaldoInsuficienteException;
import java.util.ArrayList;

public class ProcessadorPagamento {

    public static void processarPagamento(Corrida corrida) {

        if (corrida.getStatusCorrida() != StatusCorrida.FINALIZADA) {
            System.out.println("So e possível pagar corridas FINALIZADAS.");
            return;
        }

        Passageiro passageiro = corrida.getPassageiro();
        ArrayList<Pagamento> metodos = passageiro.getMetodoPagamento();

        if (metodos == null || metodos.isEmpty()) {
            System.out.println("Voce nao possui nenhum método de pagamento cadastrado.");
            return;
        }

        Pagamento metodo = metodos.get(0);

        float valor = corrida.getValorFinal();

        System.out.println("Processando pagamento via " + metodo.getTipo()
                + " no valor de R$ " + String.format("%.2f", valor));

        try {

            metodo.processar(valor);

            System.out.println("Pagamento aprovado!");
            System.out.println("Status do pagamento: " + metodo.getStatus());

        } catch (SaldoInsuficienteException e) {
            System.out.println("Pagamento recusado: saldo insuficiente.");
            System.out.println(e.getMessage());

        } catch (PagamentoRecusadoException e) {
            System.out.println("Pagamento recusado pela operadora.");
            System.out.println(e.getMessage());
        }
    }
}
