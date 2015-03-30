package javaapplication3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
public class Multicast extends Thread
{
    private final String address;
    private final InetAddress group;
    private final int port;
    
    private MulticastSocket socket;
    private byte[] contenuMessageEntrant;
    private byte[] contenuMessageSortant;
    private DatagramPacket messageEntrant;
    private DatagramPacket messageSortant;
    
    public Multicast(String address, int port) throws IOException 
    {
        this.address = address;
        this.port = port;
        group = InetAddress.getByName(this.address);
        socket = new MulticastSocket(this.port);
        socket.joinGroup(group);
    }
    
    @Override
    public void run()
    {
       
        String bufferStringSortant = "";
        String bufferStringEntrant = "";
        char bufferChar;
   
        try
        {
         
            System.out.println("Client sending messages ...");

            // Boucle principale du client
            do
            {
                bufferStringSortant = "";
                
                // Boucle d'écriture
                do
                {
                    
                    bufferChar = (char) System.in.read();
                    bufferStringSortant += bufferChar;
                                  
                }while(bufferChar!='\n');
                
                // On envoie en multicast le message
                ByteArrayOutputStream sortie = new ByteArrayOutputStream(); 
                (new DataOutputStream(sortie)).writeUTF(bufferStringSortant); 
                contenuMessageSortant = sortie.toByteArray();
                messageSortant = new DatagramPacket(contenuMessageSortant, contenuMessageSortant.length, group, 9998);
                socket.send(messageSortant);
                    
                // Lecture d'un message                
                contenuMessageEntrant = new byte[1024];
                messageEntrant = new DatagramPacket(contenuMessageEntrant, contenuMessageEntrant.length);
                try 
                {
                    socket.receive(messageEntrant);
                    bufferStringEntrant = (new DataInputStream(new ByteArrayInputStream(contenuMessageEntrant))).readUTF();
                    System.out.println(bufferStringEntrant);
                }
                catch(Exception exc)
                {
                      System.out.println(exc);
                }
                              
            }while(!"exit\n".equals(bufferStringSortant));
            
            socket.close();      
        
        } 
        
        catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } 
               
    }
            
}


