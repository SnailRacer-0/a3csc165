package a3.a3;

import a3.games.fighter2015.*;
import a3.kmap165Engine.network.*;
import java.io.IOException;
import java.net.InetAddress;

public class Starter{
   public static void main(String[] args){
      try{ 
            InetAddress theAddr = InetAddress.getLocalHost();
            String ipAddr = theAddr.getHostAddress(); 
<<<<<<< HEAD
            FightingGame fg = new FightingGame(ipAddr, 8065);
=======
            FightingGame fg = new FightingGame(ipAddr, 8080);
>>>>>>> a99e1bde9c96765ba62941d5f0e6db547d306078
            fg.start();
         }
         catch (IOException e){ 
            e.printStackTrace();
         }
      }
}