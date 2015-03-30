package javaapplication3;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nathanael
 */
public class Client extends Thread
{
    private final InetAddress address,group;
    private final int port;
    private MulticastSocket socketReception;
    
    public Client(InetAddress address, int port) throws IOException 
    {
        this.address = address;
        this.port = port;
        group = InetAddress.getByName("224.0.1.0");
        socketReception = new MulticastSocket(9998);
        socketReception.joinGroup(group);
    }
    
    @Override
    public void run()
    {
        System.out.println("Client sending messages to server...");
    
        InetSocketAddress hostAddress = new InetSocketAddress(address, port);
        SocketChannel client;
        
        String bufferString = "";
        char bufferChar;
   
        DatagramPacket message;
        byte[] contenuMessage;
        String texte;
        ByteArrayInputStream lecteur;
        
        
        
        try
        {
            Thread.sleep(2000);
            
            client = SocketChannel.open(hostAddress);
            System.out.println("Client sending messages to server...");

            // Send messages to server
            
          
            // Boucle principale du client
            do
            {
                // Boucle d'écriture
                do
                {
                    
                    bufferChar = (char) System.in.read();
                    bufferString += bufferChar;
                                  
                }while(bufferChar!='\n');
                
                byte [] sendMessage;
                sendMessage = bufferString.getBytes();
                bufferString = "";
                ByteBuffer buffer = ByteBuffer.wrap(sendMessage);
                client.write(buffer);
     
                // Lecture d'un message                
                contenuMessage = new byte[1024];
                message = new DatagramPacket(contenuMessage, contenuMessage.length);
                try 
                {
                    socketReception.receive(message);
                    texte = (new DataInputStream(new ByteArrayInputStream(contenuMessage))).readUTF();
                    System.out.println(texte);
                }
                catch(Exception exc)
                {
                      System.out.println(exc);
                }
                              
            }while(bufferString!="exit\n");
            
            client.close();      
        
        } 
        
        catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        catch (InterruptedException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
               
    }
            
}


