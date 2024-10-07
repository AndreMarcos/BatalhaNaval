/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

/**
 *
 * @author André
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JogoBatalhaNaval extends Remote {
    public void registrarJogador(String nome) throws RemoteException;
    public String atacar(int linha, int coluna) throws RemoteException;
    public String obterEstadoJogo() throws RemoteException;
}
