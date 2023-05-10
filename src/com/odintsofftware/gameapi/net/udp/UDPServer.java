/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odintsofftware.gameapi.net.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class UDPServer extends Thread {
    
    private DatagramSocket socket;
    private ArrayList<UDPConnectedClient> connectedClients = new ArrayList<UDPConnectedClient>();
    private ServerListener listener = null;
    private final int BUFFER_SIZE = 256;
    
    public UDPServer(ServerListener listener, int port) throws IOException {
        this.listener = listener;      
        this.socket = new DatagramSocket(port);                
    }
    public UDPServer(int port) throws IOException {    
        this.socket = new DatagramSocket(port);                
    }
    
    public void setListener(ServerListener listener) {
        this.listener = listener;
    }
    
    @Override
    public void run() {
        while (true) {
            byte[] data = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                
                socket.receive(packet);                
            } catch (IOException ex) {
                Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            listener.dataReceived(data, new UDPConnectedClient(packet.getAddress(), packet.getPort()));
        }
    }
    
    public void sendData(byte[] data, InetAddress destinationIp, int port) throws IOException {
        DatagramPacket packet = new DatagramPacket(data, data.length, destinationIp, port);
        socket.send(packet);
    }
    
    public void broadcast(byte[] data) throws IOException   {
        for(UDPConnectedClient client : connectedClients) {
            DatagramPacket packet = new DatagramPacket(data, data.length, client.getIpAddress(), client.getPort());
            socket.send(packet);
        }
    }
    
    public int getPort() {
        if (socket != null) return socket.getPort();
        else return -1;
    }
    
    public void addConnection(UDPConnectedClient client) {
        connectedClients.add(client);
    }
    
    public void removeConnection(UDPConnectedClient client) {
        connectedClients.remove(client);
    }
    
    public void removeAllConnections() {
        connectedClients.clear();
    }
    
    public ArrayList<UDPConnectedClient> getConnectedClients() {
        return connectedClients;
    }
    
}
