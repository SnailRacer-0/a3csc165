package a3.kmap165Engine.custom_objects.event_listener_objects;

import sage.scene.SceneNode;
import sage.scene.shape.*;
import sage.event.*;
import a3.kmap165Engine.event.CrashEvent;

import com.jogamp.common.nio.Buffers;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import a3.kmap165Engine.event.*;

public class TheChest extends Cube implements IEventListener{
   private Cube chest;
   private int oldEvent = 0;
   private Random rng = new Random();
   public TheChest(){
      chest = new Cube();
   }
   public boolean handleEvent(IGameEvent event){ 
      // if the event has programmer-defined information in it,
      // it must be cast to the programmer-defined event type.
      CrashEvent cevent = (CrashEvent) event;
      int crashCount = cevent.getWhichCrash();
      if (crashCount > oldEvent){
         this.scale(2f,4f,2f);
         oldEvent = crashCount;
      }   
      return true;
   }
}