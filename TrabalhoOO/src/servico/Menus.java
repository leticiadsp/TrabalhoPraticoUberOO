/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package servico;

import entidade.Corrida;
import java.util.Scanner;

/**
 *
 * @author Clara
 */
public interface Menus {

    public static Scanner sc = new Scanner(System.in);

    public void cadastrar();

    public void menuInicial();

    public void menuGerenciarCorrida(Corrida corrida);

}
