/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author André
 */
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class JogoBatalhaNavalImpl extends UnicastRemoteObject implements JogoBatalhaNaval {

    private static final int TAMANHO_GRADE = 10;
    private char[][] gradeJogador1 = new char[TAMANHO_GRADE][TAMANHO_GRADE];
    private char[][] gradeJogador2 = new char[TAMANHO_GRADE][TAMANHO_GRADE];
    private Map<String, char[][]> grades = new HashMap<>();
    private String jogadorAtual;
    private boolean jogoIniciado = false;

    public JogoBatalhaNavalImpl() throws RemoteException {
        super();
        inicializarGrade(gradeJogador1);
        inicializarGrade(gradeJogador2);
    }

    private void inicializarGrade(char[][] grade) {
        for (int i = 0; i < TAMANHO_GRADE; i++) {
            for (int j = 0; j < TAMANHO_GRADE; j++) {
                grade[i][j] = '-';
            }
        }
    }

    public synchronized void registrarJogador(String nome) throws RemoteException {
        if (grades.size() < 2) {
            grades.put(nome, grades.size() == 0 ? gradeJogador1 : gradeJogador2);
            System.out.println(nome + " entrou no jogo.");
            if (grades.size() == 2) {
                jogadorAtual = nome;
                jogoIniciado = true;
                System.out.println("Ambos os jogadores estão conectados. O jogo começou!");
            }
        } else {
            System.out.println("O jogo já possui dois jogadores.");
        }
    }

    public synchronized String atacar(int linha, int coluna) throws RemoteException {
        if (!jogoIniciado) {
            return "O jogo ainda não começou, aguardando o outro jogador.";
        }

        char[][] gradeOponente = jogadorAtual.equals("Jogador1") ? gradeJogador2 : gradeJogador1;

        if (gradeOponente[linha][coluna] == 'X' || gradeOponente[linha][coluna] == 'O') {
            return "Você já atacou essa posição!";
        }

        if (gradeOponente[linha][coluna] == 'N') {
            gradeOponente[linha][coluna] = 'X'; 
            trocarJogador(); 
            return "Acertou um navio!";
        } else {
            gradeOponente[linha][coluna] = 'O'; 
            trocarJogador(); 
            return "Acertou na água!";
        }
    }

    private void trocarJogador() {
        jogadorAtual = jogadorAtual.equals("Jogador1") ? "Jogador2" : "Jogador1";
    }

    public String obterEstadoJogo() throws RemoteException {
        StringBuilder estado = new StringBuilder();
        estado.append("Grade do Jogador1:\n");
        exibirGrade(gradeJogador1, estado);
        estado.append("\nGrade do Jogador2:\n");
        exibirGrade(gradeJogador2, estado);
        return estado.toString();
    }

    private void exibirGrade(char[][] grade, StringBuilder estado) {
        for (int i = 0; i < TAMANHO_GRADE; i++) {
            for (int j = 0; j < TAMANHO_GRADE; j++) {
                estado.append(grade[i][j]).append(" ");
            }
            estado.append("\n");
        }
    }

    public boolean jogoIniciado() throws RemoteException {
        return jogoIniciado;
    }
}
