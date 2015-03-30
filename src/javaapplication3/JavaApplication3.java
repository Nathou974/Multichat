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
        
        LongOpt[] longopts = new LongOpt[6];
        StringBuffer sb = new StringBuffer();
      
        
        longopts[0] = new LongOpt("address", LongOpt.REQUIRED_ARGUMENT, sb, 'a');
        longopts[1] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
        longopts[2] = new LongOpt("nio", LongOpt.NO_ARGUMENT, null, 'n');
        longopts[3] = new LongOpt("port", LongOpt.REQUIRED_ARGUMENT, sb, 'p');
        longopts[4] = new LongOpt("server", LongOpt.NO_ARGUMENT, null, 's');
        longopts[5] = new LongOpt("client", LongOpt.NO_ARGUMENT, null, 'c');
        Getopt g = new Getopt("JavaApplication3", args, "a:hnp:sc", longopts);
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
                  
                break;

                case 'p' : port = Integer.parseInt(g.getOptarg());
                break;  

                case 's' :
                    
                    if (address != "" && port !=0)
                    {
                        Server serveur =  new Server(InetAddress.getByName(address), port);
                        serveur.start();
                    }
                    else
                    {
                        Server serveur =  new Server(InetAddress.getLocalHost(), 9999);
                        serveur.start();
                    }   
                    
                break;
                
                case 'c' :
                    System.out.println("On instancie un client");
                    Client client =  new Client(InetAddress.getLocalHost(), 9999);
                    client.start();
                break;      
                    
                default:
                    System.out.println("Invalid Option");  

            }
        
        }
        

    }
    
}
