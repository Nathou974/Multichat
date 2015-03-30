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
        
        try
        {
            Thread.sleep(3000);
            
            client = SocketChannel.open(hostAddress);
            System.out.println("Client sending messages to server...");

            // Send messages to server

            String [] messages = new String [] {"Time goes fast.", "What now?", "Bye."};

            for (int i = 0; i < messages.length; i++)
            {
                byte [] message = new String(messages [i]).getBytes();
                ByteBuffer buffer = ByteBuffer.wrap(message);
                client.write(buffer);

                System.out.println(messages [i]);
                buffer.clear();
                Thread.sleep(3000);
            }

            //client.close();      
        
        } 
        
        catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        catch (InterruptedException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /* DatagramPacket message;
        byte[] contenuMessage;
        String texte;
        ByteArrayInputStream lecteur;
    
        while(true) 
        {
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
        }*/
    }
            
}


