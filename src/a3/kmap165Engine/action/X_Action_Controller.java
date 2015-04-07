package a3.kmap165Engine.action;

import sage.input.action.AbstractInputAction;
import sage.app.AbstractGame;
import net.java.games.input.Event;
import sage.camera.*;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import graphicslib3D.Matrix3D;
import sage.scene.SceneNode;
import sage.scene.shape.*;

public class X_Action_Controller extends AbstractInputAction{ 
   private SceneNode s;
   private Matrix3D sM;
   public X_Action_Controller(SceneNode sn){ 
      s = sn;
      sM = s.getLocalTranslation();
   }
   public void performAction(float time, Event e){
          
      if (e.getValue() < -0.2){ 
         sM.translate(-0.1f,0,0);
         s.setLocalTranslation(sM);
         s.updateWorldBound();
      }
      else { 
         if (e.getValue() > 0.2){
            sM.translate(0.1f,0,0);
            s.setLocalTranslation(sM);
            s.updateWorldBound();
         }    
      } 
   }
}