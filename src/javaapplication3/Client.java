package javaapplication3;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
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
    private final InetAddress address;
    private final int port;
 
    public Client(InetAddress address, int port) throws IOException 
    {
        this.address = address;
        this.port = port;
      
    }
    
    @Override
    public void run()
    {
        System.out.println("Client sending messages to server...");
    
        InetSocketAddress hostAddress = new InetSocketAddress(address, port);
        SocketChannel client;
        
        String bufferString = "";
        char bufferChar;
        
        try
        {
            Thread.sleep(2000);
            
            client = SocketChannel.open(hostAddress);
            System.out.println("Client sending messages to server...");

            // Boucle principale du client
            do
            {
                // Boucle d'écriture                 
                bufferString = "";
                
                do
                {
                    
                    bufferChar = (char) System.in.read();
                    bufferString += bufferChar;
                                  
                }while(bufferChar!='\n');
                
                // Lecture
                
                byte [] sendMessage;
                sendMessage = bufferString.getBytes();
              
                ByteBuffer buffer = ByteBuffer.wrap(sendMessage);
                client.write(buffer);
     
                ByteBuffer bufferReceive = ByteBuffer.allocate(8192);
                client.read(bufferReceive);
                Charset charset = Charset.defaultCharset();
                bufferReceive.flip();
                CharBuffer cbuf = charset.decode(bufferReceive);
                System.out.println("Message read from server: " + cbuf);
                bufferReceive.compact();
                              
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


