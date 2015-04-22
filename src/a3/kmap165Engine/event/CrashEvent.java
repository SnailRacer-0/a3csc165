package a3.kmap165Engine.event;

import sage.event.*;
import sage.event.AbstractGameEvent;

public class CrashEvent extends AbstractGameEvent{ 
   private int whichCrash;
   public CrashEvent(int n) { 
      whichCrash = n;
   }
   public int getWhichCrash() { 
      return whichCrash; 
   }
}