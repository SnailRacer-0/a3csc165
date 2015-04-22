package a3.kmap165Engine.scene_node_controller;

import sage.scene.Controller;
import sage.scene.SceneNode;
import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;

public class MyTranslate_RotateController extends Controller{
   private double translationRate = .003 ; // movement per second
   private double cycleTime = 1000.0; // default cycle time
   private double totalTime;
   private double direction = 1.0;
   private boolean first = true;
   public void update(double time){
 
      totalTime += time;
      double transAmount = translationRate * time ;
      if (totalTime > cycleTime){
         direction = -direction;
         totalTime = 0.0;
         if(first){
            first = false;
            translationRate = .006;
         }
      }
      transAmount = direction * transAmount;
      Matrix3D newTrans = new Matrix3D();
      newTrans.translate(transAmount,0,0);
      double rotAmount = -0.25 % 360;
       Matrix3D newRot = new Matrix3D();
      newRot.rotate(rotAmount, new Vector3D(0,1,0));
     
      for (SceneNode node : controlledNodes){
         Matrix3D curTrans = node.getLocalTranslation();
         curTrans.concatenate(newTrans);
         node.setLocalTranslation(curTrans);
         Matrix3D curRot = node.getLocalRotation();
         curRot.concatenate(newRot);
         node.setLocalRotation(curRot);
      }
   }
}