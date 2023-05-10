/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odintsofftware.gameapi.net.udp;

/**
 *
 * Interface to receive data from the UDPClient
 * 
 * @author Ivan
 */
public interface ClientListener {
    
    public void dataReceived(byte[] data);
    
}
