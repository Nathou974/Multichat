/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication3;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server
{
    InetAddress address;
    int port;

    ServerSocket server;
    Socket client;
    
    private ArrayList<OutputStream> OutputStreamArray = new ArrayList<OutputStream>();

    public Server(InetAddress address, int port)
    {
            this.address = address;
            this.port = port;
    }
	
    public void start() throws IOException
    {
        server = new ServerSocket();
        server.bind(server.getLocalSocketAddress(), port);

        System.out.println("inet address: " + address);
        //System.out.println("port: " + port);
        System.out.println("server address: " + server.getLocalSocketAddress());

        while(true)
        {
            System.out.println("attente de client");
            client = server.accept();
            System.out.println("client accepté");
           
            new Thread(new ClientServer(client)).start();
        }
    }
    
    public synchronized void removeClient (OutputStream streamOut)
    {
        for(OutputStream stream : OutputStreamArray)
        {
            if(stream == streamOut)
            {
               OutputStreamArray.remove(stream);
            }
        }
    }
    
    public void sendToAll(String message, OutputStream streamOut) throws IOException
    {
        for(OutputStream stream : OutputStreamArray)
        {
            if(stream != streamOut)
            {
                for(int i=0; i<message.length(); i++)
                {
                    stream.write(message.charAt(i));    
                }
            }
        }
    }

    public class ClientServer implements Runnable
    {
        Socket client;

        InputStream streamIn;
        OutputStream streamOut;

        String bufferString, bufferStringConnexion, nickname;
        char bufferChar;

        public ClientServer(Socket client)
        {
                this.client = client;
        }

        public void run()
        {
            int i;
            try
            {
                nickname = "";
                streamIn = client.getInputStream();
                streamOut = client.getOutputStream();

                OutputStreamArray.add(streamOut);

                bufferString = "bienvenue\n";
                bufferStringConnexion = "Un client s'est connecte !\n";

                sendToAll(bufferStringConnexion,streamOut);

                for(i=0; i<bufferString.length(); i++)
                {
                    streamOut.write(bufferString.charAt(i));
                }
                
                while(!bufferString.equals(nickname +": exit\n"))
                {
                    if (nickname != "")
                    {
                         bufferString = nickname + ": ";
                    }

                    else
                    {
                        bufferString = "";
                    }

                    bufferChar = 'a';

                    while(bufferChar != '\n')
                    {                              
                        bufferChar = (char) streamIn.read();
                        bufferString += bufferChar;
                    }

                    if (bufferString.startsWith("/nick"))
                    {
                        nickname = bufferString.substring(6, bufferString.length()-1);
                    }

                    else
                    {

                    System.out.print(bufferString);
                    sendToAll(bufferString,streamOut);
                    
                    }
                }
                
                if (nickname != "")
                {
                    sendToAll(nickname+" s'est deconnecte du chat !\n",streamOut);
                }
                
                else
                {
                    sendToAll("Une personne anonyme s'est deconnectee du chat !\n",streamOut);
                }
                
                removeClient(streamOut);   
                client.close();            
            }
            
            catch(IOException e)
            {
                    e.printStackTrace();
            }
        }
    }
}