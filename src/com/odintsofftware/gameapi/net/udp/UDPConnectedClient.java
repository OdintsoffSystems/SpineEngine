/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odintsofftware.gameapi.net.udp;

import java.net.InetAddress;

/**
 *
 * @author root
 */
public class UDPConnectedClient {
    
    private InetAddress ipAddress;
    private int port;

    public UDPConnectedClient(InetAddress ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }
    
    /**
     * @return the ipAddress
     */
    public InetAddress getIpAddress() {
        return ipAddress;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }
        
}
