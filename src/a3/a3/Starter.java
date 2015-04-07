package a3.a3;

import a3.games.fighter2015.*;
import a3.kmap165Engine.network.*;
import java.io.IOException;

public class Starter{
   public static void main(String[] args){
      try{ 
            FightingGame fg = new FightingGame("130.86.65.83", 80);
            fg.start();
         }
         catch (IOException e){ 
            e.printStackTrace();
         }
      }
}