/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author André
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;

public class ClienteGUI extends JFrame {
    private JButton[][] botoesAtaque;
    private JButton[][] botoesDefesa;
    private JogoBatalhaNaval jogo;
    private String nomeJogador;
    private int naviosRestantes = 5; 

    public ClienteGUI() {
        setTitle("Batalha Naval");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1));

        JPanel painelAtaque = new JPanel(new GridLayout(10, 10));
        JPanel painelDefesa = new JPanel(new GridLayout(10, 10));

        botoesAtaque = new JButton[10][10];
        botoesDefesa = new JButton[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                botoesAtaque[i][j] = new JButton();
                botoesAtaque[i][j].setBackground(Color.BLUE);
                painelAtaque.add(botoesAtaque[i][j]);

                botoesDefesa[i][j] = new JButton();
                botoesDefesa[i][j].setBackground(Color.GREEN);
                painelDefesa.add(botoesDefesa[i][j]);

                int linha = i;
                int coluna = j;
                
                botoesDefesa[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        posicionarNavio(linha, coluna);
                    }
                });
                
                botoesAtaque[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        realizarAtaque(linha, coluna);
                    }
                });
            }
        }

        add(painelAtaque);
        add(painelDefesa);

        conectarAoServidor();
        definirNomeJogador();
    }

    private void conectarAoServidor() {
        try {
            jogo = (JogoBatalhaNaval) Naming.lookup("rmi://localhost/BatalhaNaval");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao servidor: " + e.getMessage());
        }
    }

    private void definirNomeJogador() {
        nomeJogador = JOptionPane.showInputDialog(this, "Informe seu nome:");
        try {
            jogo.registrarJogador(nomeJogador);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar jogador: " + e.getMessage());
        }
    }

    private void posicionarNavio(int linha, int coluna) {
        if (naviosRestantes > 0) {
            botoesDefesa[linha][coluna].setBackground(Color.GRAY); 
            naviosRestantes--;
            if (naviosRestantes == 0) {
                JOptionPane.showMessageDialog(this, "Todos os navios foram posicionados!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Todos os navios já foram posicionados.");
        }
    }

    private void realizarAtaque(int linha, int coluna) {
        try {
            if (naviosRestantes > 0) {
                JOptionPane.showMessageDialog(this, "Você deve posicionar todos os navios antes de atacar.");
                return;
            }
            String resultado = jogo.atacar(linha, coluna);
            JOptionPane.showMessageDialog(this, "Resultado do ataque: " + resultado);
            atualizarEstadoJogo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao realizar ataque: " + e.getMessage());
        }
    }

    private void atualizarEstadoJogo() {
        try {
            String estado = jogo.obterEstadoJogo();
            System.out.println(estado);  
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar estado do jogo: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ClienteGUI().setVisible(true);
            }
        });
    }
}