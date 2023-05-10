/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odintsofftware.gameapi.net.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public class Server {

    private final int PORT = 701;
    private ArrayList<ClientConnection> connections = new ArrayList<ClientConnection>();
    private boolean clone = false;

    public Server() throws IOException, Exception {
        consoleLog("Starting up server in port " + PORT);

        System.out.print(" -> Ligando servidor...");
        ServerSocket s = null;
        try {
            s = new ServerSocket(PORT);
        } catch (Exception io) {
            consoleLog(" !! Erro criando ServerSocket:");            
        }
        consoleLog("Ok.");

        consoleLog(" -> Servidor rodando na porta " + String.valueOf(PORT) + ".");
        consoleLog(" ** Pressione Ctrl+C a qualquer momento para finalizar. **");

        while (true) {

            // checar por novas conexões
            try {
                if (!s.toString().isEmpty()) {
                    Socket incomingConnection = s.accept();
                    String ipCliente = incomingConnection.getInetAddress().toString();
                    System.out.print(" -> Recebendo conexao de " + ipCliente.substring(1) + "...");

                    /**
                     * @todo Sistema de detecção de clones, não funcionando
                     * mesmo comparando Strings ou InetAddress.
                     */
                    /*
                     * int i=0; while (i < conexoes.size()) {
                     * System.out.print("."); ConexaoCliente checaIp =
                     * (ConexaoCliente) conexoes.elementAt(i); String ipc1 =
                     * checaIp.getIp().toString(); String ipc2 =
                     * conecta.getInetAddress().toString(); if (ipc2 == ipc1) {
                     * exibe("clone!"); clone = true; break; } i++; }
                     *
                     * if (clone == false) { exibe("Ok"); exibe(" -> Cliente
                     * conectado com sucesso."); ConexaoCliente cliente = new
                     * ConexaoCliente(conecta); conexoes.addElement(cliente); }
                     * else if (clone == true) { exibe("clone!"); clone = false;
                     * }
                     */
                    consoleLog("Ok");
                    consoleLog(" -> Cliente conectado com sucesso.");
                    ClientConnection client = new ClientConnection(incomingConnection);
                    connections.add(client);

                }
            } catch (IOException io) {
                consoleLog(" !! Erro aceitando conexão, o server será finalizado");
                throw io;
            }

            // While para receber dados de clientes
            int i = 0;
            while (i < connections.size()) {
                ClientConnection receive = (ClientConnection) connections.get(i);
                try {
                    String data = receive.receiveData();
                    if (data == null) {
                        receive.closeConnection();
                        connections.remove(i);
                        connections.trimToSize();
                        break;
                    }
                    if (!data.isEmpty()) {
                        int j = 0;
                        while (j < connections.size()) {
                            if (j != i) {
                                ClientConnection send = (ClientConnection) connections.get(j);
                                send.sendData(data);
                            }
                        }
                    }
                } catch (Exception ex) {
                    consoleLog(" !! Erro recebendo dados de " + receive.getIp().toString().substring(1) + " - cliente desconectado");
                    connections.remove(i);
                    throw ex;
                }
            }

        } // fim do laço infinito
    }

    private void consoleLog(String msg) {
        System.out.println(msg);
    }
}
