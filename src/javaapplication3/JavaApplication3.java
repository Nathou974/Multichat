/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication3;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.IntBuffer;

/**
 *
 * @author nathanael
 */
public class JavaApplication3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        // TODO code application logic here
        
        int port = 0;
        String address="";
        
        boolean nio =false, server=false, clientbool=false;
                
        LongOpt[] longopts = new LongOpt[7];
        StringBuffer sb = new StringBuffer();
        
        
        longopts[0] = new LongOpt("address", LongOpt.REQUIRED_ARGUMENT, sb, 'a');
        longopts[1] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
        longopts[2] = new LongOpt("nio", LongOpt.NO_ARGUMENT, null, 'n');
        longopts[3] = new LongOpt("port", LongOpt.REQUIRED_ARGUMENT, sb, 'p');
        longopts[4] = new LongOpt("server", LongOpt.NO_ARGUMENT, null, 's');
        longopts[5] = new LongOpt("client", LongOpt.NO_ARGUMENT, null, 'c');
        longopts[6] = new LongOpt("multicast", LongOpt.NO_ARGUMENT, null, 'm');
        Getopt g = new Getopt("JavaApplication3", args, "a:hnp:scm", longopts);
        int c;
        
        while((c = g.getopt()) != -1)
        {
            
            switch(c)
            {
                case 'a' : address = g.getOptarg();
                break;

                case 'h' :
                  
                break;

                case 'n' :
                    nio = true;
                    /*System.out.println("On instancie un serveur NIO");
                    if (!"".equals(address) && port !=0)
                    {
                        ServerNIO serveurNio =  new ServerNIO(InetAddress.getByName(address), port);
                        serveurNio.start();
                    }
                    else
                    {
                        ServerNIO serveurNio =  new ServerNIO(InetAddress.getLocalHost(), 9999);
                        serveurNio.start();
                    }*/
                break;

                case 'p' : port = Integer.parseInt(g.getOptarg());
                break;  

                case 's' :
                    server = true;
                    /*System.out.println("On instancie un serveur");
                    if (!"".equals(address) && port !=0)
                    {
                        Server serveur =  new Server(InetAddress.getByName(address), port);
                        serveur.start();
                    }
                    else
                    {
                        Server serveur =  new Server(InetAddress.getLocalHost(), 9999);
                        serveur.start();
                    }   */
                    
                break;
                
                case 'c' :
                    clientbool = true;
                    /*System.out.println("On instancie un client pour serveur NIO");
                    Client client =  new Client(InetAddress.getLocalHost(), 9999);
                    client.start();*/
                break;   
                    
                case 'm' :
                    System.out.println("On instancie un client multicast");
                    Multicast multi = new Multicast("239.255.10.10",9998);
                    multi.start();
                break;  
                    
                default:
                    System.out.println("Invalid Option");  

            }
            
        }   
            if(server && !nio && !clientbool)
            {
                System.out.println("On instancie un serveur");
                
                if (!"".equals(address) && port !=0)
                {
                    Server serveur =  new Server(InetAddress.getByName(address), port);
                    serveur.start();     
                }
                else
                {
                    Server serveur =  new Server(InetAddress.getLocalHost(), 9999);
                    serveur.start();
                }   
           
            }
            
            else if(nio && !server)
            {
                System.out.println("On instancie un serveur NIO");
                
                if (!"".equals(address) && port !=0)
                {
                    ServerNIO serveurNio =  new ServerNIO(InetAddress.getByName(address), port);
                    serveurNio.start();
                    if (clientbool)
                    {
                    System.out.println("On instancie un client pour serveur NIO");
                    Client client =  new Client(InetAddress.getByName(address), port);
                    client.start();
                    }
                }
                else
                {
                    ServerNIO serveurNio =  new ServerNIO(InetAddress.getLocalHost(), 9999);
                    serveurNio.start();
                    if (clientbool)
                    {
                    System.out.println("On instancie un client pour serveur NIO");
                    Client client =  new Client(InetAddress.getLocalHost(), 9999);
                    client.start();
                    }
                }
            }
            
            else if(nio && server && clientbool || server && nio || server && clientbool)
            {
                System.out.println("Veuillez saisir des arguments corrects");
            }
         

    }
    
}
