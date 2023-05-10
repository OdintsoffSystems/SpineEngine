/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odintsofftware.gameapi.net.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class UDPClient extends Thread {
    
    private DatagramSocket socket;   
    private ClientListener listener = null;
    private final int BUFFER_SIZE = 1024;
    private final InetAddress ipAddress;
    private final int port;
    
    public UDPClient(ClientListener listener, InetAddress ipAddress, int port) throws IOException {
        this.listener = listener;      
        this.ipAddress = ipAddress;
        this.port = port;
        this.socket = new DatagramSocket();                
    }
    
    public UDPClient(InetAddress ipAddress, int port) throws IOException {
        this.ipAddress = ipAddress;
        this.port = port;        
        this.socket = new DatagramSocket();                
    }
    
    public void setListener(ClientListener listener) {
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
                Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            listener.dataReceived(data);
        }
    }
    
    public void sendData(byte[] data) throws IOException {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        socket.send(packet);
    }        
    
    public int getConnectedPort() {
        return port;
    }
    
    public InetAddress getConnectedIpAddress() {
        return ipAddress;
    }
    
}
