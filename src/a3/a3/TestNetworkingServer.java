package a3.a3;

import a3.kmap165Engine.network.*;
import java.io.IOException;

public class TestNetworkingServer { 
   public static void main(String[] args)throws IOException{
      try{ 
<<<<<<< HEAD
         GameServerTCP testTCPServer = new GameServerTCP(8065);
=======
         GameServerTCP testTCPServer = new GameServerTCP(8080);
>>>>>>> a99e1bde9c96765ba62941d5f0e6db547d306078
      }
      catch (IOException e){ 
         e.printStackTrace();
      }        
   }
}