/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odintsofftware.gameapi.net.udp;

import java.net.InetAddress;

/**
 *
 * @author Ivan
 */
public class CommandPacket {
    
    private InetAddress ipAddress;
    private int port;
    private int command;
    private String commandString;
    private String message;
    
    
    /*
     *  To receive packets with identification
     */
    public CommandPacket(byte[] data, InetAddress ipAddress, int port) {      
        this.ipAddress = ipAddress;
        this.port = port;        
        String dataString = new String(data);
        commandString = dataString.substring(0,2);        
        
        int commandInt = -1;
        
        try {
            commandInt = Integer.parseInt(commandString);
        }
        catch (NumberFormatException nfe) 
        {
            //cannot parse, packet is invalid
            command = -1; 
        }
        
        if (isValid()) {
            this.command = commandInt;
            this.message = dataString.substring(2);            
        }
        
    }
    
    public CommandPacket(byte[] data, UDPConnectedClient client) {
        this.ipAddress = client.getIpAddress();
        this.port = client.getPort();        
        String dataString = new String(data).trim();
        commandString = dataString.substring(0,2);
        
        int commandInt = -1;
        
        try {
            commandInt = Integer.parseInt(commandString);
        }
        catch (NumberFormatException nfe) 
        {
            //cannot parse, packet is invalid
            command = -1; 
        }
        
        if (isValid()) {
            this.command = commandInt;
            this.message = dataString.substring(2);            
        }
        
    }
    
    public CommandPacket(String dataString, InetAddress ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;        
        commandString = dataString.substring(0,2);
        
        int commandInt = -1;
        
        try {
            commandInt = Integer.parseInt(commandString);
        }
        catch (NumberFormatException nfe) 
        {
            //cannot parse, packet is invalid
            command = -1; 
        }
        
        if (isValid()) {
            this.command = commandInt;
            this.message = dataString.substring(2);            
        }
        
    }
    
    public CommandPacket(String message, int command, InetAddress ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;        
        
        if (command > 99 || command < 0) this.command = -1;
        else  {
            this.command = command;
            this.commandString = String.format("%02d", command);
        }       
                
        if (isValid()) {
            this.message = message;            
        }
        
    }
    
    /*
     * To send packets or receive without origin
     */
    
    public CommandPacket(byte[] data) {
        String dataString = new String(data);
        commandString = dataString.substring(0,2);
        
        int commandInt = -1;
        
        try {
            commandInt = Integer.parseInt(commandString);
        }
        catch (NumberFormatException nfe) 
        {
            //cannot parse, packet is invalid
            command = -1; 
        }
        
        if (isValid()) {
            this.command = commandInt;
            this.commandString = String.format("%02d", command);
            this.message = dataString.substring(2);            
        }
        
    }
    
    public CommandPacket(String dataString) {     
        commandString = dataString.substring(0,2);
        
        int commandInt = -1;
        
        try {
            commandInt = Integer.parseInt(commandString);
        }
        catch (NumberFormatException nfe) 
        {
            //cannot parse, packet is invalid
            command = -1; 
        }
        
        if (isValid()) {
            this.command = commandInt;
            this.message = dataString.substring(2);            
        }
        
    }
    
    public CommandPacket(String message, int command) {
          
        if (command > 99 || command < 0) this.command = -1;
        else { 
            this.command = command;
            this.commandString = String.format("%02d", command);
        }
                
        if (isValid()) {
            this.message = message;            
        }
        
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

    /**
     * @return the command
     */
    public int getCommand() {
        return command;
    }
    
    /**
     * @return the message
     */
    public String getMessage() {
        return message.trim();
    }
            
    /**
     * @return if packet is valid
     */
    public final boolean isValid() {
        if (command == -1) return false;
        else return true;
    }
    
    public final byte[] toBytes() {
        if (isValid())
            return (commandString+message).getBytes();
        else 
            return null;
    }
    
}
