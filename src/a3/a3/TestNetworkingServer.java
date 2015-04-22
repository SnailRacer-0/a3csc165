package a3.a3;

import a3.kmap165Engine.network.*;
import java.io.IOException;

public class TestNetworkingServer { 
   public static void main(String[] args)throws IOException{
      try{ 
         GameServerTCP testTCPServer = new GameServerTCP(8065);

      }
      catch (IOException e){ 
         e.printStackTrace();
      }        
   }
}