/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odintsofftware.gameapi.net.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author root
 */
public class ClientConnection {

  private BufferedReader inFromClient;
  private DataOutputStream outToClient;
  private Socket client;
  private final InetAddress CLIENT_IP;
//  Thread recebe = new Thread();
//  private Vector comandos;
//  private int millis_per_tick = 10; // msec


  public ClientConnection(Socket client)
  {
    this.CLIENT_IP = client.getInetAddress();
    try
    {
      inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
      outToClient = new DataOutputStream(client.getOutputStream());
    }
    catch (Exception ex)
    { System.out.println(" !! Erro criando sockete para " + CLIENT_IP.toString().substring(1) + "!" );
      try {closeConnection();} catch (Exception ex2) {}
    }
  }

  public final void closeConnection() throws Exception
  {
    client.close();
    System.out.println(" !! Conexao com " + CLIENT_IP.toString().substring(1) + " fechada.");
  }

  public InetAddress getIp()
  {
    return CLIENT_IP;
  }

  public void sendData(String data) throws Exception
  {
    outToClient.writeBytes(data + "\n");
  }

  public String receiveData() throws Exception
  {
    return inFromClient.readLine();
  }
}
