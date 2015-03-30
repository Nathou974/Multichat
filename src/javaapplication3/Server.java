/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication3;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nathanael
 */
public class Server extends Thread implements MultichatServer
{
    
    private InetAddress address;
    private int port;
    
    public Server(InetAddress address, int port)
    {
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() 
    {
      
    ServerSocketChannel serverSocketChannel;

    
    try 
    {
        InetAddress group = InetAddress.getByName("224.0.1.0");
        MulticastSocket s = new MulticastSocket(9998);
        s.joinGroup(group);
           
        System.out.println(InetAddress.getLocalHost());
        Selector selector = Selector.open();
        System.out.println("Selector open: " + selector.isOpen());
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(address, port));
        System.out.println(serverSocketChannel.getLocalAddress());
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        
        
        
        while(true)
        {
            
            System.out.println("Waiting for select...");
            int noOfKeys = selector.select();	 
           
            System.out.println("Number of selected keys: " + noOfKeys);	 
           
            Set<SelectionKey> selectedKeys = selector.selectedKeys();

            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while(keyIterator.hasNext())
            {
                
                SelectionKey key = keyIterator.next();

                if(key.isAcceptable()) {
                    // a connection was accepted by a ServerSocketChannel.
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                   
                } else if (key.isConnectable()) {
                    // a connection was established with a remote server.
                    System.out.println("isConnectable");

                } else if (key.isReadable()) {
                    // a channel is ready for reading
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(8192);
                    client.read(buffer);
                    Charset charset = Charset.defaultCharset();
                    buffer.flip();
                    CharBuffer cbuf = charset.decode(buffer);
                    System.out.println("Message read from client: " + cbuf);
                    buffer.compact();
                    
                    if (cbuf.toString().equals("exit\n"))
                    {
                        client.close();
	                System.out.println("Client messages are complete; close.");
	            }
	 
                 
                    byte[] contenuMessage;
                    DatagramPacket message;
	
                    ByteArrayOutputStream sortie = new ByteArrayOutputStream(); 
                    
                    (new DataOutputStream(sortie)).writeUTF(cbuf.toString()); 
                    contenuMessage = sortie.toByteArray();
		    message = new DatagramPacket(contenuMessage, contenuMessage.length, group, 9998);
                    s.send(message);

                } else if (key.isWritable()) {
                    // a channel is ready for writing
                    System.out.println("isWritable");
                }

                keyIterator.remove();

            }
   
        }

    } 
    
    catch (IOException ex) 
    {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
    }

    }

  
}
